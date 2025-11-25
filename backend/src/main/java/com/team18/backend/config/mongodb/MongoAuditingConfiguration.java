package com.team18.backend.config.mongodb;

import com.team18.backend.service.wrapper.AuditService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoAuditingConfiguration {

    private final AuditService auditService;

    public MongoAuditingConfiguration( AuditService auditService) {
        this.auditService = auditService;
    }

    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(auditService.getCurrentTimestamp());
    }
}
