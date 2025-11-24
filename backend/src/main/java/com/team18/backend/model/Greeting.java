package com.team18.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "greetings")
public record Greeting(@Id String id, String message) {}

