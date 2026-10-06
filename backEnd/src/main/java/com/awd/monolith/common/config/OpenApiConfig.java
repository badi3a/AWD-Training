package com.awd.monolith.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI awdOpenApi(@Value("${server.port:8080}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("AWD Recruitment API (monolith)")
                        .version("1.0.0")
                        .description("Monolithic RESTful version of the AWD project: candidate management, " +
                                "job management and candidature management in one Spring Boot application " +
                                "with one MySQL database. Starting point before the migration to microservices.")
                        .contact(new Contact().name("Badia Abouhdid")))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }

    // One group per business module: they are the future microservices.
    // The dropdown at the top right of Swagger UI switches between them.

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("0-all").pathsToMatch("/api/**").build();
    }

    @Bean
    public GroupedOpenApi candidatApi() {
        return GroupedOpenApi.builder().group("1-candidat")
                .packagesToScan("com.awd.monolith.candidat.controller").build();
    }

    @Bean
    public GroupedOpenApi jobApi() {
        return GroupedOpenApi.builder().group("2-job")
                .packagesToScan("com.awd.monolith.job.controller").build();
    }

    @Bean
    public GroupedOpenApi candidatureApi() {
        return GroupedOpenApi.builder().group("3-candidature")
                .packagesToScan("com.awd.monolith.candidature.controller").build();
    }
}
