package com.team18.backend.dto;

import com.team18.backend.model.ShipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update shipment status")
public record ShipmentStatusUpdateDTO(
        @NotNull(message = "Status is required")
        @Schema(
                description = "New shipment status",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        ShipmentStatus status
) {
}

