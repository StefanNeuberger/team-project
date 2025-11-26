package com.team18.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItemDTO( @NotBlank(message = "item name is required field")
                       @Size(min = 2, max = 100, message = "name must be between 2 and 100 characters")
                       @Schema(
                               description = "Name of the itemDto",
                               example = "Widget A",
                               requiredMode = Schema.RequiredMode.REQUIRED,
                               nullable = false,
                               minLength = 2,
                               maxLength = 100
                       )
                       String name,
                       @Schema(
                               description = "Sku of the item",
                               example = "SKU12345",
                               requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                               minLength = 2,
                               maxLength = 100
                       )
                       String sku ) {
}
