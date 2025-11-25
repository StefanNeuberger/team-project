package com.team18.backend.greeting;

import com.team18.backend.UnitTestContainersConfiguration;
import com.team18.backend.repository.GreetingRepository;

import com.team18.backend.model.Greeting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(UnitTestContainersConfiguration.class)
class GreetingRepositoryTest {

    @Autowired
    private GreetingRepository greetingRepository;

    @Test
    void savesAndRetrievesGreetingViaMongoContainer() {
        Greeting saved = greetingRepository.save( new Greeting( null, "Hello, Testcontainers!" ) );

        assertThat( saved.getId() ).isNotNull();
        Optional<Greeting> expected = greetingRepository.findById( saved.getId() );
        assertThat( expected ).isPresent();

        assertThat( expected.get())
                .extracting( "id", "message" )
                .contains( saved.getId(), saved.getMessage() );

        assertThat( expected.get().getCreatedDate() ).isNotNull();
        assertThat( expected.get().getLastModifiedDate() ).isNotNull().isEqualTo( expected.get().getCreatedDate() );
    }
}

