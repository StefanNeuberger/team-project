package com.team18.backend.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryCreateDTO(
        @Schema(
                description = "Quantity of the related item",
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minimum = "1"
        )
        @NotNull
        @Min(1)
        Integer quantity
) {
}
