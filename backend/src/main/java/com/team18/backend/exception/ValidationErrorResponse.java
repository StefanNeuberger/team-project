package com.team18.backend.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
    String message,
    int status,
    String error,
    Instant timestamp,
    Map<String, String> errors
) {}

