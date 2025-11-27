package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Document(collection = "shipments")
@Schema(description = "Shipment entity")
@CompoundIndex(def = "{'warehouse.$id': 1, 'status': 1}")
public class Shipment extends BaseModel {

    @NotNull(message = "Warehouse is required")
    @DocumentReference
    @Schema(
            description = "Related warehouse entity",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private Warehouse warehouse;

    @NotNull(message = "Expected arrival date is required")
    @Schema(
            description = "Expected arrival date",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private LocalDate expectedArrivalDate;

    @NotNull(message = "Status is required")
    @Indexed
    @Schema(
            description = "Shipment status",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private ShipmentStatus status;

    public Shipment() {
        super( null );
    }

    public Shipment( String id, Warehouse warehouse, LocalDate expectedArrivalDate, ShipmentStatus status ) {
        super( id );
        this.warehouse = warehouse;
        this.expectedArrivalDate = expectedArrivalDate;
        this.status = status;
    }

    public Shipment( Warehouse warehouse, LocalDate expectedArrivalDate, ShipmentStatus status ) {
        this( null, warehouse, expectedArrivalDate, status );
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse( Warehouse warehouse ) {
        this.warehouse = warehouse;
    }

    public LocalDate getExpectedArrivalDate() {
        return expectedArrivalDate;
    }

    public void setExpectedArrivalDate( LocalDate expectedArrivalDate ) {
        this.expectedArrivalDate = expectedArrivalDate;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus( ShipmentStatus status ) {
        this.status = status;
    }
}

