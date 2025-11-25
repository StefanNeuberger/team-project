package com.team18.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "shops")
public class Shop extends BaseModel {

    private String name;

    public Shop( String id, String name ) {
        super( id );
        this.name = name;
    }

    public Shop( String name ) {
        this( null, name );
    }

    public String getName() {
        return name;
    }
}

