package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "shops")
@Schema(description = "Shop entity")
public class Shop extends BaseModel {


    @NotBlank(message = "Shop name is required")
    @Size(min = 2, max = 100, message = "Shop name must be between 2 and 100 characters")
    @Indexed(unique = true)
    @Schema(
            description = "Shop name",
            example = "Tech Store",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minLength = 2,
            maxLength = 100
    )
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

