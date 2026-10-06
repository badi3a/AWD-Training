package com.awd.monolith.candidat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Address returned by the API")
public record AddressResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Avenue Habib Bourguiba")
        String street,

        @Schema(example = "12B")
        String houseNumber,

        @Schema(example = "1001")
        String zipCode,

        @Schema(description = "Id of the candidate living at this address, null if unassigned", example = "1", nullable = true)
        Long candidateId
) {
}
