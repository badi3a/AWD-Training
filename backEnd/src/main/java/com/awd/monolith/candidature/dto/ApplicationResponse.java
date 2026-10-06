package com.awd.monolith.candidature.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Candidature returned by the API, with a summary of the candidate and the job")
public record ApplicationResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "2026-09-29", type = "string", format = "date")
        LocalDate applicationDate,

        @Schema(example = "I have 2 years of experience with Spring Boot microservices.")
        String motivation,

        CandidateSummary candidate,

        JobSummary job
) {

    @Schema(name = "ApplicationCandidate", description = "Candidate who applied")
    public record CandidateSummary(
            @Schema(example = "1") Long id,
            @Schema(example = "Badia") String firstname,
            @Schema(example = "Abouhdid") String lastname,
            @Schema(example = "badia@example.com") String email) {
    }

    @Schema(name = "ApplicationJob", description = "Job applied to")
    public record JobSummary(
            @Schema(example = "1") Long id,
            @Schema(example = "Java Spring Boot Developer") String name,
            @Schema(example = "true") boolean available,
            @Schema(example = "Software Development") String category) {
    }
}
