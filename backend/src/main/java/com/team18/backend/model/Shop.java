package com.team18.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;
import io.swagger.v3.oas.annotations.media.Schema;



@Document(collection = "shops")
@Schema(description = "Shop entity")
public class Shop extends BaseModel {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, nullable = false)
    private String name;

    public Shop() {
        super( null );
    }

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

