package com.team18.backend.dto;

import com.team18.backend.model.BaseModel;

import java.util.Objects;

public class ItemDTO extends BaseModel {
    private String sku;
    private String name;

    public ItemDTO( String sku, String name ) {
        super( null );
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
        return "ItemDTO{" +
                "sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals( Object o ) {
        if ( o == null || getClass() != o.getClass() ) return false;
        ItemDTO itemDTO = ( ItemDTO ) o;
        return Objects.equals( sku, itemDTO.sku ) && Objects.equals( name, itemDTO.name );
    }

    @Override
    public int hashCode() {
        return Objects.hash( sku, name );
    }
}
