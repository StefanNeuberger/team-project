package com.team18.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.team18.backend.model.Greeting;

public interface GreetingRepository extends MongoRepository<Greeting, String> {
}

