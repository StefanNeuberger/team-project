package com.team18.backend.exception;

import java.time.Instant;

public record ErrorResponse(
    String message,
    int status,
    String error,
    Instant timestamp
) {}

