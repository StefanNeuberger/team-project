package com.team18.backend.dto.inventory;

import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.model.Item;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record InventoryResponseDTO(
        @Schema(
                description = "The unique identifier of this entity.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        String id,
        @Schema(
                description = "Related warehouse entity",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        WarehouseResponseDTO warehouse,
        @Schema(
                description = "Related item entity",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Item item,
        @Schema(
                description = "Quantity of the related item",
                accessMode = Schema.AccessMode.READ_ONLY,
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minimum = "1"
        )
        Integer quantity,
        @Schema(
                description = "The timestamp when this entity was created.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Instant createdDate,
        @Schema(
                description = "The timestamp of the last modification of this entity.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Instant lastModifiedDate
) {
}
