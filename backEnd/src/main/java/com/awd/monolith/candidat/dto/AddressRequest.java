package com.awd.monolith.candidat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Data used to create or update an address")
public record AddressRequest(

        @Schema(description = "Street name", example = "Avenue Habib Bourguiba")
        @NotBlank(message = "street is required")
        @Size(max = 150)
        String street,

        @Schema(description = "House number", example = "12B")
        @NotBlank(message = "houseNumber is required")
        @Size(max = 10)
        String houseNumber,

        @Schema(description = "Zip / postal code", example = "1001")
        @NotBlank(message = "zipCode is required")
        @Pattern(regexp = "^[A-Za-z0-9 -]{3,10}$", message = "zipCode must be 3 to 10 letters, digits, spaces or dashes")
        String zipCode
) {
}
