package com.awd.candidat;

import com.fasterxml.jackson.databind.JsonNode;
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

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CandidatApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private static final String CANDIDATE_WITH_ADDRESS = """
            {"firstname":"Badia","lastname":"Abouhdid","email":"badia@example.com",
             "address":{"street":"Avenue Habib Bourguiba","houseNumber":"12","zipCode":"1001"}}
            """;

    private long post(String url, String body) throws Exception {
        String json = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode node = mapper.readTree(json);
        return node.get("id").asLong();
    }

    @Test
    void createCandidateWithAddressAndReadIt() throws Exception {
        long id = post("/api/candidates", CANDIDATE_WITH_ADDRESS);

        mvc.perform(get("/api/candidates/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("badia@example.com"))
                .andExpect(jsonPath("$.address.zipCode").value("1001"))
                .andExpect(jsonPath("$.address.candidateId").value(id));

        mvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void validationErrorsReturn400() throws Exception {
        mvc.perform(post("/api/candidates").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"\",\"lastname\":\"X\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstname").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void duplicateEmailReturns409() throws Exception {
        post("/api/candidates", CANDIDATE_WITH_ADDRESS);
        mvc.perform(post("/api/candidates").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"A\",\"lastname\":\"B\",\"email\":\"BADIA@example.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void unknownIdReturns404() throws Exception {
        mvc.perform(get("/api/candidates/999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/addresses/999")).andExpect(status().isNotFound());
    }

    @Test
    void updateCandidateAndAddress() throws Exception {
        long id = post("/api/candidates", CANDIDATE_WITH_ADDRESS);
        mvc.perform(put("/api/candidates/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstname":"Badia","lastname":"A.","email":"badia@example.com",
                                 "address":{"street":"Rue de Marseille","houseNumber":"5","zipCode":"2000"}}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("A."))
                .andExpect(jsonPath("$.address.street").value("Rue de Marseille"));

        // the existing address was updated in place, not duplicated
        mvc.perform(get("/api/addresses")).andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void assignUnlinkAndDeleteAddress() throws Exception {
        long candidateId = post("/api/candidates",
                "{\"firstname\":\"Sami\",\"lastname\":\"Ben\",\"email\":\"sami@example.com\"}");
        long addressId = post("/api/addresses",
                "{\"street\":\"Rue de Rome\",\"houseNumber\":\"3\",\"zipCode\":\"1000\"}");

        mvc.perform(put("/api/candidates/" + candidateId + "/address/" + addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address.id").value(addressId));

        // a second candidate cannot take the same address (one-to-one)
        long other = post("/api/candidates",
                "{\"firstname\":\"Lina\",\"lastname\":\"K\",\"email\":\"lina@example.com\"}");
        mvc.perform(put("/api/candidates/" + other + "/address/" + addressId))
                .andExpect(status().isConflict());

        // unlink keeps the address
        mvc.perform(delete("/api/candidates/" + candidateId + "/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").doesNotExist());
        mvc.perform(get("/api/addresses/" + addressId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidateId").doesNotExist());

        // re-link, then deleting the address unlinks it from the candidate
        mvc.perform(put("/api/candidates/" + candidateId + "/address/" + addressId)).andExpect(status().isOk());
        mvc.perform(delete("/api/addresses/" + addressId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/candidates/" + candidateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").doesNotExist());
    }

    @Test
    void deletingCandidateAlsoDeletesItsAddress() throws Exception {
        long id = post("/api/candidates", CANDIDATE_WITH_ADDRESS);
        mvc.perform(delete("/api/candidates/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/candidates/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/addresses")).andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void swaggerDocsAreExposed() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Candidat Microservice API"))
                .andExpect(jsonPath("$.paths['/api/candidates']").exists())
                .andExpect(jsonPath("$.paths['/api/addresses/{id}']").exists());
    }
}
