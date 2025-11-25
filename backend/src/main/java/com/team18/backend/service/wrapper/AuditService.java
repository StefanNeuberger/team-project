package com.team18.backend.service.wrapper;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditService {
    public Instant getCurrentTimestamp() {
        return Instant.now();
    }
}
