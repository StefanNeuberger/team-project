package com.team18.backend.greeting;

import com.team18.backend.TestcontainersConfiguration;
import com.team18.backend.repository.GreetingRepository;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;

import com.team18.backend.model.Greeting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(TestcontainersConfiguration.class)
class GreetingRepositoryTest {

    @Autowired
    private GreetingRepository greetingRepository;

    @Test
    void savesAndRetrievesGreetingViaMongoContainer() {
        Greeting saved = greetingRepository.save( new Greeting( null, "Hello, Testcontainers!" ) );

        assertThat( saved.id() ).isNotNull();
        java.util.Optional<Greeting> expected = greetingRepository.findById( saved.id() );
        assertThat( expected ).isPresent();
        assertThat( expected.get().message() ).isEqualTo( "Hello, Testcontainers!" );
    }
}

