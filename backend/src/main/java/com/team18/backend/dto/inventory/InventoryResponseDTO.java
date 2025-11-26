package com.team18.backend.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record InventoryResponseDTO(
        @Schema(description = "The unique identifier of this entity.", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false)
        String id,
        @Schema(
                description = "Quantity of the related item",
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minimum = "1"
        )
        Integer quantity,
        @Schema(description = "The timestamp when this entity was created.", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false)
        Instant createdDate,
        @Schema(description = "The timestamp of the last modification of this entity.", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false)
        Instant lastModifiedDate
) {
}
