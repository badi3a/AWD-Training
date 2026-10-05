package com.awd.candidat.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI candidatOpenApi(@Value("${server.port:8081}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Candidat Microservice API")
                        .version("1.0.0")
                        .description("REST API to manage candidates and their addresses " +
                                "(one-to-one relationship: each candidate has at most one address).")
                        .contact(new Contact().name("Badia Abouhdid")))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }
}
