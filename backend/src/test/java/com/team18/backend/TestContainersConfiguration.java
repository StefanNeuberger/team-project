package com.team18.backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Shared Testcontainers configuration for integration tests.
 * Provides a MongoDB container that is automatically configured via @ServiceConnection.
 * This is used by @SpringBootTest tests that need a real database.
 *
 * Note: For @DataMongoTest, use UnitTestContainersConfiguration instead,
 * which includes additional test-specific auditing configuration.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer( DockerImageName.parse( "mongo:7.0.14" ) );
    }
}
