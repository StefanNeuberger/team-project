package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("items")
@Schema(description = "Item entity")
public class Item extends BaseModel {

    @Indexed(unique = true)
    @Schema(
            description = "Sku of the item",
            example = "SKU12345",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            minLength = 2,
            maxLength = 100
    )
    private String sku;

    @NotBlank(message = "Item name must not be empty")
    @Size(min = 2, max = 100, message = "name must be between 2 and 100 characters")
    @Indexed(unique = true)
    @Schema(
            description = "Name of the item",
            example = "Widget A",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minLength = 2,
            maxLength = 100
    )
    private String name;

    @PersistenceCreator
    public Item( String id, String sku, String name ) {
        super( id );
        this.sku = sku;
        this.name = name;
    }

    public Item() {
        super( null );
    }

    public String getSku() {
        return sku;
    }

    public void setSku( String sku ) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals( Object o ) {
        if ( o == null || getClass() != o.getClass() ) return false;
        Item item = ( Item ) o;
        return Objects.equals( sku, item.sku ) && Objects.equals( name, item.name );
    }

    @Override
    public int hashCode() {
        return Objects.hash( sku, name );
    }
}

