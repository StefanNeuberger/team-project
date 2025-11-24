package com.team18.backend.service;

import org.springframework.stereotype.Service;

import com.team18.backend.repository.GreetingRepository;

import com.team18.backend.model.Greeting;


@Service
public class GreetingService {

    private final GreetingRepository repository;

    public GreetingService( GreetingRepository repository ) {
        this.repository = repository;
    }

    public Greeting createGreeting( String message ) {
        return repository.save( new Greeting( null, message ) );
    }

}

