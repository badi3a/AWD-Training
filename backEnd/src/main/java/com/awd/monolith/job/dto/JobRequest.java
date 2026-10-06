package com.awd.monolith.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Data used to create or update a job")
public record JobRequest(

        @Schema(description = "Job title", example = "Java Spring Boot Developer")
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Schema(description = "Job description", example = "Build microservices with Spring Boot")
        @Size(max = 1000, message = "description must be at most 1000 characters")
        String description,

        @Schema(description = "Whether the job is still open", example = "true")
        @NotNull(message = "available is required")
        Boolean available,

        @Schema(description = "Publication date (yyyy-MM-dd)", example = "2026-09-28", type = "string", format = "date")
        @NotNull(message = "date is required")
        LocalDate date,

        @Schema(description = "Id of the job's category", example = "1")
        @NotNull(message = "categoryId is required")
        Long categoryId
) {
}
