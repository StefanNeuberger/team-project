package com.team18.backend;

import com.team18.backend.service.wrapper.AuditService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Optional;

@TestConfiguration(proxyBeanMethods = false)
@EnableMongoAuditing(dateTimeProviderRef = "testDateTimeProvider")
public class UnitTestContainersConfiguration {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer( DockerImageName.parse( "mongo:7.0.14" ) );
    }

    @Bean
    public AuditService auditService() {
        AuditService mock = Mockito.mock( AuditService.class );
        Mockito.when(mock.getCurrentTimestamp()).thenReturn( Instant.now());
        return mock;
    }

    @Bean
    public DateTimeProvider testDateTimeProvider( AuditService auditService) {
        return () -> Optional.of(auditService.getCurrentTimestamp());
    }

}
