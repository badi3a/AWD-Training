package com.awd.monolith.candidature.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Data used to update a candidature. The candidate and the job cannot be changed: " +
        "delete the candidature and create a new one instead.")
public record ApplicationUpdateRequest(

        @Schema(description = "Motivation text", example = "Updated motivation.")
        @Size(max = 2000, message = "motivation must be at most 2000 characters")
        String motivation,

        @Schema(description = "Application date (yyyy-MM-dd)", example = "2026-09-29", type = "string", format = "date")
        @NotNull(message = "applicationDate is required")
        @PastOrPresent(message = "applicationDate cannot be in the future")
        LocalDate applicationDate
) {
}
