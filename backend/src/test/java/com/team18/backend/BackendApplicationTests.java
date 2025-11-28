package com.team18.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestContainersConfiguration.class)
@SpringBootTest("spring.shell.interactive.enabled=false")
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
