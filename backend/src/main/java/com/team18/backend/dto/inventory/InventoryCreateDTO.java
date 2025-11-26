package com.team18.backend.dto.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InventoryCreateDTO(
        @NotNull
        @NotBlank
        @NotEmpty
        @Schema(
                description = "Related warehouse entity",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        String warehouseId,
        @NotNull
        @NotBlank
        @NotEmpty
        @Schema(
                description = "Related item entity",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false
        )
        String itemId,
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
