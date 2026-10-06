package com.awd.monolith;

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
class AwdMonolithApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    // ---------- helpers ----------

    private long post(String url, String body) throws Exception {
        String json = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(json).get("id").asLong();
    }

    private long candidate(String email) throws Exception {
        return post("/api/candidates", """
                {"firstname":"Badia","lastname":"Abouhdid","email":"%s",
                 "address":{"street":"Avenue Habib Bourguiba","houseNumber":"12","zipCode":"1001"}}
                """.formatted(email));
    }

    private long job(String name, boolean available) throws Exception {
        long cat = post("/api/categories", "{\"name\":\"Cat " + name + "\"}");
        return post("/api/jobs", """
                {"name":"%s","description":"d","available":%s,"date":"2026-09-01","categoryId":%d}
                """.formatted(name, available, cat));
    }

    private String application(long candidateId, long jobId) {
        return """
                {"candidateId":%d,"jobId":%d,"motivation":"Motivated","applicationDate":"2026-09-20"}
                """.formatted(candidateId, jobId);
    }

    // ---------- candidat & job modules ----------

    @Test
    void candidateAndJobCrudStillWork() throws Exception {
        long c = candidate("a@example.com");
        mvc.perform(get("/api/candidates/" + c))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.zipCode").value("1001"));

        long j = job("Java Dev", true);
        mvc.perform(get("/api/jobs/" + j))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.name").value("Cat Java Dev"));
    }

    // ---------- candidature module ----------

    @Test
    void applyToJobAndReadIt() throws Exception {
        long c = candidate("a@example.com");
        long j = job("Java Dev", true);
        long id = post("/api/applications", application(c, j));

        mvc.perform(get("/api/applications/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivation").value("Motivated"))
                .andExpect(jsonPath("$.applicationDate").value("2026-09-20"))
                .andExpect(jsonPath("$.candidate.id").value(c))
                .andExpect(jsonPath("$.candidate.email").value("a@example.com"))
                .andExpect(jsonPath("$.job.id").value(j))
                .andExpect(jsonPath("$.job.category").value("Cat Java Dev"));
    }

    @Test
    void applicationDateDefaultsToToday() throws Exception {
        long c = candidate("a@example.com");
        long j = job("Java Dev", true);
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"candidateId\":%d,\"jobId\":%d}".formatted(c, j)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationDate").value(java.time.LocalDate.now().toString()));
    }

    @Test
    void cannotApplyTwiceOrToClosedJob() throws Exception {
        long c = candidate("a@example.com");
        long open = job("Open", true);
        long closed = job("Closed", false);

        post("/api/applications", application(c, open));
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(application(c, open)))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(application(c, closed)))
                .andExpect(status().isConflict());
    }

    @Test
    void validationAndUnknownIds() throws Exception {
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.candidateId").exists())
                .andExpect(jsonPath("$.errors.jobId").exists());

        long j = job("Java Dev", true);
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(application(999, j)))
                .andExpect(status().isNotFound());
        mvc.perform(get("/api/applications/999")).andExpect(status().isNotFound());
    }

    @Test
    void filterByCandidateAndJob() throws Exception {
        long c1 = candidate("a@example.com");
        long c2 = candidate("b@example.com");
        long j1 = job("Java Dev", true);
        long j2 = job("Angular Dev", true);
        post("/api/applications", application(c1, j1));
        post("/api/applications", application(c1, j2));
        post("/api/applications", application(c2, j1));

        mvc.perform(get("/api/applications")).andExpect(jsonPath("$.length()").value(3));
        mvc.perform(get("/api/applications?candidateId=" + c1)).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/applications?jobId=" + j1)).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/candidates/" + c2 + "/applications")).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/jobs/" + j2 + "/applications")).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateAndDeleteApplication() throws Exception {
        long c = candidate("a@example.com");
        long j = job("Java Dev", true);
        long id = post("/api/applications", application(c, j));

        mvc.perform(put("/api/applications/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motivation\":\"New text\",\"applicationDate\":\"2026-09-21\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivation").value("New text"))
                .andExpect(jsonPath("$.applicationDate").value("2026-09-21"));

        mvc.perform(delete("/api/applications/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/applications/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void candidateOrJobWithApplicationsCannotBeDeleted() throws Exception {
        long c = candidate("a@example.com");
        long j = job("Java Dev", true);
        long id = post("/api/applications", application(c, j));

        mvc.perform(delete("/api/candidates/" + c)).andExpect(status().isConflict());
        mvc.perform(delete("/api/jobs/" + j)).andExpect(status().isConflict());

        mvc.perform(delete("/api/applications/" + id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/candidates/" + c)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/jobs/" + j)).andExpect(status().isNoContent());
    }

    @Test
    void swaggerDocsExposeAllModules() throws Exception {
        mvc.perform(get("/v3/api-docs/0-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("AWD Recruitment API (monolith)"))
                .andExpect(jsonPath("$.paths['/api/candidates']").exists())
                .andExpect(jsonPath("$.paths['/api/jobs']").exists())
                .andExpect(jsonPath("$.paths['/api/applications']").exists());
        mvc.perform(get("/v3/api-docs/3-candidature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/applications/{id}']").exists());
    }
}
