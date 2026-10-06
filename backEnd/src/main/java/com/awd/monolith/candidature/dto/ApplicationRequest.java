package com.awd.monolith.candidature.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Data used to create a candidature")
public record ApplicationRequest(

        @Schema(description = "Id of the candidate who applies", example = "1")
        @NotNull(message = "candidateId is required")
        Long candidateId,

        @Schema(description = "Id of the job the candidate applies to", example = "1")
        @NotNull(message = "jobId is required")
        Long jobId,

        @Schema(description = "Motivation text", example = "I have 2 years of experience with Spring Boot microservices.")
        @Size(max = 2000, message = "motivation must be at most 2000 characters")
        String motivation,

        @Schema(description = "Application date (yyyy-MM-dd). Defaults to today.", example = "2026-09-29",
                type = "string", format = "date", nullable = true)
        @PastOrPresent(message = "applicationDate cannot be in the future")
        LocalDate applicationDate
) {
}
