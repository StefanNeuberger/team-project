package com.team18.backend.dto.shipmentlineitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


@Schema(description = "ShipmentLineItem for creation")
public record ShipmentLineItemCreateDTO(
        @NotNull(message = "ShipmentId is required")
        @Schema(
                description = "Related shipment entity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        String shipmentId,
        @NotNull(message = "ItemId is required")
        @Schema(
                description = "Related item entity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        String itemId,
        @Schema(
                description = "Expected item quantity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        Integer expectedQuantity,
        @Schema(
                description = "Received item quantity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        Integer receivedQuantity
) {
}
