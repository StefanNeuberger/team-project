package com.team18.backend.dto.inventory;

import com.team18.backend.dto.validators.NullOrMin;
import io.swagger.v3.oas.annotations.media.Schema;

public record InventoryUpdateDTO(
        @Schema(
                description = "Quantity of the related item",
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minimum = "1"
        )
        @NullOrMin(min = 1)
        Integer quantity
) {
}
