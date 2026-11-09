package com.awd.job.config;

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
    public OpenAPI jobOpenApi(@Value("${server.port:8082}") String port) {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Microservice API")
                        .version("1.0.0")
                        .description("REST API to manage jobs and their categories " +
                                "(one category has many jobs, each job belongs to exactly one category).")
                        .contact(new Contact().name("Badia Abouhdid")))
                .servers(List.of(new Server().url("http://localhost:" + port).description("Local")));
    }
}
