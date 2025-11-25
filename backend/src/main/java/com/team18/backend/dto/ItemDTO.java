package com.team18.backend.dto;

import com.team18.backend.model.BaseModel;

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

}
