package com.team18.backend.config.mongodb;

import com.team18.backend.service.wrapper.AuditService;
import org.junit.jupiter.api.Test;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MongoAuditingConfigurationTest {

    private final MongoAuditingConfiguration config = new MongoAuditingConfiguration( new AuditService() );

    @Test
    void testDateTimeProviderReturnsCurrentTime() {
        DateTimeProvider provider = config.dateTimeProvider();

        Instant result = (Instant) provider.getNow().orElse( null );

        assertNotNull(result);
        assertTrue(result.isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void testDateTimeProviderNeverReturnsNull() {
        DateTimeProvider provider = config.dateTimeProvider();

        Instant result = (Instant) provider.getNow().orElse( null );

        assertNotNull(result);
    }

}