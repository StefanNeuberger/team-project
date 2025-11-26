package com.team18.backend.dto;

import com.team18.backend.model.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Request to update shipment details")
public record ShipmentUpdateDTO(
        @Schema(
                description = "Expected arrival date",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        LocalDate expectedArrivalDate,

        @Schema(
                description = "Shipment status",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        ShipmentStatus status
) {
}

