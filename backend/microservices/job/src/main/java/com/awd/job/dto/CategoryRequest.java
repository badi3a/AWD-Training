package com.awd.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Data used to create or update a category")
public record CategoryRequest(

        @Schema(description = "Unique category name", example = "Software Development")
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Schema(description = "Category description", example = "Backend, frontend and mobile development jobs")
        @Size(max = 500, message = "description must be at most 500 characters")
        String description
) {
}
