package com.awd.monolith.candidat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Candidate returned by the API")
public record CandidateResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Badia")
        String firstname,

        @Schema(example = "Abouhdid")
        String lastname,

        @Schema(example = "badia@example.com")
        String email,

        @Schema(nullable = true)
        AddressResponse address
) {
}
