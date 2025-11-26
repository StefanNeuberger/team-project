package com.team18.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemDTO( @NotBlank(message = "item name is required field") String name, String sku ) {
}
