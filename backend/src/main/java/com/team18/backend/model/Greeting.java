package com.team18.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "greetings")
public class Greeting extends BaseModel {
    private final String message;

    public Greeting( String id, String message ) {
        super( id );
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

