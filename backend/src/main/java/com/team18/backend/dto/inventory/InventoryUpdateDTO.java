package com.team18.backend.dto.inventory;

import com.team18.backend.dto.validators.NullOrMin;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record InventoryUpdateDTO(
        @Nullable
        @Schema(
                description = "Related warehouse entity",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String warehouseId,
        @Nullable
        @Schema(
                description = "Related item entity",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String itemId,
        @Schema(
                description = "Quantity of the related item",
                example = "20",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true,
                minimum = "1"
        )
        @NullOrMin(min = 1)
        Integer quantity
) {
}
