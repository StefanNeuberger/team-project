package com.team18.backend.dto;

import com.team18.backend.model.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Request to create a new shipment")
public record ShipmentCreateDTO(
        @NotBlank(message = "Warehouse ID is required")
        @Schema(
                description = "Delivery destination warehouse ID",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String warehouseId,

        @NotNull(message = "Expected arrival date is required")
        @Schema(
                description = "Expected arrival date",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate expectedArrivalDate,

        @NotNull(message = "Status is required")
        @Schema(
                description = "Initial shipment status",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ShipmentStatus status
) {
}

