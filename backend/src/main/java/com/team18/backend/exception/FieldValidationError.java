package com.team18.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a single field validation error")
public record FieldValidationError(
        @Schema(description = "Field name that failed validation", example = "organizationName")
        String field,
        @Schema(description = "Validation error message", example = "This field must be unique")
        String message
) {
}
