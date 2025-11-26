package com.team18.backend.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("items")
public class Item extends BaseModel {

    private String sku;
    @NotBlank(message = "name must not be empty")
    private String name;

    public Item( String id, String sku, String name ) {
        super( id );
        this.sku = sku;
        this.name = name;
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

