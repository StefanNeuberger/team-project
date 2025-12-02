package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Document(collection = "shipmentLineItems")
@Schema(description = "ShipmentLineItem entity")
public class ShipmentLineItem extends BaseModel {

    @NotNull(message = "Shipment is required")
    @DocumentReference
    @Schema(
            description = "Related shipment entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false
    )
    private Shipment shipment;

    @NotNull(message = "Item is required")
    @DocumentReference
    @Schema(
            description = "Related item entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false
    )
    private Item item;

    @NotNull(message = "Expected quantity is required")
    @Schema(
            description = "Related item entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false,
            minimum = "1"
    )
    private Integer expectedQuantity;

    @NotNull(message = "Received quantity is required")
    @Schema(
            description = "Related item entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false,
            minimum = "0"
    )
    private Integer receivedQuantity;

    @PersistenceCreator
    public ShipmentLineItem( String id, Instant createdDate, Instant lastModifiedDate, Shipment shipment, Item item, Integer expectedQuantity, Integer receivedQuantity ) {
        super( id, createdDate, lastModifiedDate );
        this.shipment = shipment;
        this.item = item;
        this.expectedQuantity = expectedQuantity;
        this.receivedQuantity = receivedQuantity;
    }

    public ShipmentLineItem( Shipment shipment, Item item, Integer expectedQuantity, Integer receivedQuantity ) {
        this.shipment = shipment;
        this.item = item;
        this.expectedQuantity = expectedQuantity;
        this.receivedQuantity = receivedQuantity;
    }

    public ShipmentLineItem( String id, Shipment shipment, Item item, Integer expectedQuantity, Integer receivedQuantity ) {
        super( id );
        this.shipment = shipment;
        this.item = item;
        this.expectedQuantity = expectedQuantity;
        this.receivedQuantity = receivedQuantity;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Item getItem() {
        return item;
    }

    public Integer getExpectedQuantity() {
        return expectedQuantity;
    }

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setShipment( Shipment shipment ) {
        this.shipment = shipment;
    }

    public void setItem( Item item ) {
        this.item = item;
    }

    public void setExpectedQuantity( Integer expectedQuantity ) {
        this.expectedQuantity = expectedQuantity;
    }

    public void setReceivedQuantity( Integer receivedQuantity ) {
        this.receivedQuantity = receivedQuantity;
    }

}