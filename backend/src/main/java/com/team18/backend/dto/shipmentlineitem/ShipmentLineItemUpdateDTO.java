package com.team18.backend.dto.shipmentlineitem;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ShipmentLineItem for partial updates")
public record ShipmentLineItemUpdateDTO(
        @Schema(
                description = "Related shipment entity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String shipmentId,
        @Schema(
                description = "Related item entity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String itemId,
        @Schema(
                description = "Expected item quantity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        Integer expectedQuantity,
        @Schema(
                description = "Received item quantity",
                accessMode = Schema.AccessMode.READ_WRITE,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        Integer receivedQuantity
) {
}
