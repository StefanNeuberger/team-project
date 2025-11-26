package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "shipments")
@Schema(description = "Shipment entity")
public class Shipment extends BaseModel {

    @NotBlank(message = "Warehouse ID is required")
    @Schema(
            description = "Delivery destination warehouse ID",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private String warehouseId;

    @NotNull(message = "Expected arrival date is required")
    @Schema(
            description = "Expected arrival date",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private LocalDate expectedArrivalDate;

    @NotNull(message = "Status is required")
    @Schema(
            description = "Shipment status",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false
    )
    private ShipmentStatus status;

    public Shipment() {
        super( null );
    }

    public Shipment( String id, String warehouseId, LocalDate expectedArrivalDate, ShipmentStatus status ) {
        super( id );
        this.warehouseId = warehouseId;
        this.expectedArrivalDate = expectedArrivalDate;
        this.status = status;
    }

    public Shipment( String warehouseId, LocalDate expectedArrivalDate, ShipmentStatus status ) {
        this( null, warehouseId, expectedArrivalDate, status );
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId( String warehouseId ) {
        this.warehouseId = warehouseId;
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

