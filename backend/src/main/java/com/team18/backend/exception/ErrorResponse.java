package com.team18.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ErrorResponse(
        @Schema(example = "Resource not found")
        String message,
        
        @Schema(example = "404")
        int status,

        @Schema(example = "Not Found")
        String error,

        Instant timestamp
) {
}