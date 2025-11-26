package com.team18.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record FieldValidationErrorResponse(
        @Schema(description = "Human readable error message")
        String message,
        @Schema(description = "HTTP status code")
        int status,
        @Schema(description = "Textual status description")
        String error,
        @Schema(description = "Timestamp of the error")
        Instant timestamp,
        @Schema(description = "List of field-level validation errors")
        List<FieldValidationError> fieldErrors
) {
}

