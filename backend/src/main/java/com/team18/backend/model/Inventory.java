package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(value = "inventory")
public class Inventory extends BaseModel {

    //TODO: DocumentReferences to Product and Warehouse

    @Schema(
            description = "Quantity of the related item",
            example = "20",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minimum = "1"
    )
    private int quantity;

    @PersistenceCreator
    public Inventory( String id, int quantity, Instant createdDate, Instant lastModifiedDate ) {
        super( id, createdDate, lastModifiedDate );
        this.quantity = quantity;
    }

    public Inventory() {
        super( null );
    }

    public Inventory( String id, int quantity ) {
        super( id );
        this.quantity = quantity;
    }

    public Inventory( int quantity ) {
        super( null );
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
