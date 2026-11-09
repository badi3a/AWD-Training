package com.awd.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Runs against in-memory H2 (see src/test/resources/application.properties), no MySQL needed. */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JobApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private long post(String url, String body) throws Exception {
        String json = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(json).get("id").asLong();
    }

    private long category(String name) throws Exception {
        return post("/api/categories", "{\"name\":\"" + name + "\",\"description\":\"desc\"}");
    }

    private long job(String name, boolean available, String date, long categoryId) throws Exception {
        return post("/api/jobs", """
                {"name":"%s","description":"d","available":%s,"date":"%s","categoryId":%d}
                """.formatted(name, available, date, categoryId));
    }

    @Test
    void createJobAndReadItWithItsCategory() throws Exception {
        long cat = category("Software Development");
        long id = job("Java Developer", true, "2026-09-01", cat);

        mvc.perform(get("/api/jobs/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java Developer"))
                .andExpect(jsonPath("$.date").value("2026-09-01"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.category.id").value(cat));
    }

    @Test
    void filterJobsByAvailabilityAndCategory() throws Exception {
        long dev = category("Dev");
        long data = category("Data");
        job("A", true, "2026-09-01", dev);
        job("B", false, "2026-09-02", dev);
        job("C", true, "2026-09-03", data);

        mvc.perform(get("/api/jobs")).andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("C")); // newest first
        mvc.perform(get("/api/jobs?available=true")).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/jobs?categoryId=" + dev)).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/jobs?available=true&categoryId=" + dev)).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/categories/" + data + "/jobs")).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateAndCloseJob() throws Exception {
        long dev = category("Dev");
        long data = category("Data");
        long id = job("A", true, "2026-09-01", dev);

        mvc.perform(put("/api/jobs/" + id).contentType(MediaType.APPLICATION_JSON).content("""
                        {"name":"A2","description":"new","available":true,"date":"2026-09-05","categoryId":%d}
                        """.formatted(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("A2"))
                .andExpect(jsonPath("$.category.id").value(data));

        mvc.perform(patch("/api/jobs/" + id + "/availability?available=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void validationAndNotFound() throws Exception {
        mvc.perform(post("/api/jobs").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"available\":true}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.date").exists())
                .andExpect(jsonPath("$.errors.categoryId").exists());

        // unknown category
        mvc.perform(post("/api/jobs").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"X\",\"available\":true,\"date\":\"2026-09-01\",\"categoryId\":999}"))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/jobs/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/categories/999")).andExpect(status().isNotFound());
    }

    @Test
    void duplicateCategoryNameReturns409() throws Exception {
        category("Dev");
        mvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"dev\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void categoryWithJobsCannotBeDeleted() throws Exception {
        long cat = category("Dev");
        long id = job("A", true, "2026-09-01", cat);

        mvc.perform(delete("/api/categories/" + cat)).andExpect(status().isConflict());
        mvc.perform(delete("/api/jobs/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/categories/" + cat)).andExpect(status().isNoContent());
    }

    @Test
    void swaggerDocsAreExposed() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Job Microservice API"))
                .andExpect(jsonPath("$.paths['/api/jobs']").exists())
                .andExpect(jsonPath("$.paths['/api/categories/{id}/jobs']").exists());
    }
}
