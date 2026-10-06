package com.awd.monolith.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Job returned by the API")
public record JobResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Java Spring Boot Developer")
        String name,

        @Schema(example = "Build microservices with Spring Boot")
        String description,

        @Schema(example = "true")
        boolean available,

        @Schema(example = "2026-09-28", type = "string", format = "date")
        LocalDate date,

        CategoryResponse category
) {
}
