package com.awd.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Category returned by the API")
public record CategoryResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Software Development")
        String name,

        @Schema(example = "Backend, frontend and mobile development jobs")
        String description
) {
}
