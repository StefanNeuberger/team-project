package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Document(value = "inventory")
public class Inventory extends BaseModel {

    @Schema(
            description = "Related warehouse entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    @DocumentReference
    private Warehouse warehouse;

    @Schema(
            description = "Related item entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    @DocumentReference
    private Item item;

    @Schema(
            description = "Quantity of the related item",
            example = "20",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minimum = "1"
    )
    private int quantity;

    @PersistenceCreator
    public Inventory( String id, Warehouse warehouse, Item item, int quantity, Instant createdDate, Instant lastModifiedDate ) {
        super( id, createdDate, lastModifiedDate );
        this.warehouse = warehouse;
        this.item = item;
        this.quantity = quantity;
    }

    public Inventory() {
        super( null );
    }

    public Inventory( String id, Warehouse warehouse, Item item, int quantity ) {
        super( id );
        this.warehouse = warehouse;
        this.item = item;
        this.quantity = quantity;
    }

    public Inventory( Warehouse warehouse, Item item, int quantity ) {
        super( null );
        this.warehouse = warehouse;
        this.item = item;
        this.quantity = quantity;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity( int quantity ) {
        this.quantity = quantity;
    }
}
