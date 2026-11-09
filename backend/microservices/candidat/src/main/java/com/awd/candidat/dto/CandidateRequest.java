package com.awd.candidat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data used to create or update a candidate")
public record CandidateRequest(

        @Schema(description = "First name", example = "Badia")
        @NotBlank(message = "firstname is required")
        @Size(max = 50)
        String firstname,

        @Schema(description = "Last name", example = "Abouhdid")
        @NotBlank(message = "lastname is required")
        @Size(max = 50)
        String lastname,

        @Schema(description = "Unique email address", example = "badia@example.com")
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,

        @Schema(description = "Optional address. On update, an address given here replaces the current one; " +
                "if omitted, the current address is kept.")
        @Valid
        AddressRequest address
) {
}
