package com.team18.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;

public record WarehouseCreateDTO(
        @NotBlank(message = "Warehouse name is required")
        @Schema(
                description = "Warehouse name",
                example = "Warehouse EU East",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minLength = 1
        )
        String name,
        @Nullable
        @Schema(
                description = "Latitude of the warehouse location",
                example = "12.43424",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        Double lat,
        @Nullable
        @Schema(
                description = "Longitude of the warehouse location",
                example = "23.43424",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        Double lng,
        @Nullable
        @Schema(
                description = "Street name",
                example = "Sonnenallee",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String street,
        @Nullable
        @Schema(
                description = "Number of the street",
                example = "202",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String number,
        @Nullable
        @Schema(
                description = "City name",
                example = "Berlin",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String city,
        @Nullable
        @Schema(
                description = "Postal code",
                example = "13357",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String postalCode,
        @Nullable
        @Schema(
                description = "State name",
                example = "Berlin",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String state,
        @Nullable
        @Schema(
                description = "Country name",
                example = "Germany",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        String country,
        @NotNull @Min(1)
        @Schema(
                description = "Maximum capacity of a warehouse",
                example = "223002",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minimum = "1"
        )
        Integer maxCapacity
) {
}
