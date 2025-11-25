package com.team18.backend;

import org.springframework.boot.SpringApplication;

public class TestBackendApplication {

    public static void main( String[] args ) {
        SpringApplication.from( BackendApplication::main ).with( TestContainersConfiguration.class ).run( args );
    }

}
