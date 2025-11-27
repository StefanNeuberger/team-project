package com.team18.backend.dto;

import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Shipment response with all details")
public record ShipmentResponseDTO(
        @Schema(description = "Unique identifier", accessMode = Schema.AccessMode.READ_ONLY)
        String id,

        @Schema(
                description = "Delivery destination warehouse ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String warehouseId,

        @Schema(
                description = "Expected arrival date",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate expectedArrivalDate,

        @Schema(
                description = "Shipment status",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ShipmentStatus status,

        @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
        Instant createdDate,

        @Schema(description = "Last modification timestamp", accessMode = Schema.AccessMode.READ_ONLY)
        Instant lastModifiedDate
) {
    public static ShipmentResponseDTO fromEntity( Shipment shipment ) {
        return new ShipmentResponseDTO(
                shipment.getId(),
                shipment.getWarehouse() != null ? shipment.getWarehouse().getId() : null,
                shipment.getExpectedArrivalDate(),
                shipment.getStatus(),
                shipment.getCreatedDate(),
                shipment.getLastModifiedDate()
        );
    }
}

