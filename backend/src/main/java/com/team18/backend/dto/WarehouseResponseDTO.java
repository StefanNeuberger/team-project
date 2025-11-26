package com.team18.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public record WarehouseResponseDTO(
        String id,
        @Schema(
                description = "Warehouse name",
                example = "Warehouse EU East",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minLength = 1
        )
        String name,
        @JsonIgnore
        @Schema(hidden = true)
        Double lat,
        @JsonIgnore
        @Schema(hidden = true)
        Double lng,
        @JsonIgnore
        @Schema(hidden = true)
        String street,
        @JsonIgnore
        @Schema(hidden = true)
        String number,
        @JsonIgnore
        @Schema(hidden = true)
        String city,
        @JsonIgnore
        @Schema(hidden = true)
        String postalCode,
        @JsonIgnore
        @Schema(hidden = true)
        String state,
        @JsonIgnore
        @Schema(hidden = true)
        String country,
        @Schema(
                description = "Maximum capacity of a warehouse",
                example = "223002",
                requiredMode = Schema.RequiredMode.REQUIRED,
                nullable = false,
                minLength = 1
        )
        Integer maxCapacity
) {

    @Schema(
            description = "The geographic location as [lat, lng]",
            example = "[12.3223213, 23.2133123]",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    public Double[] getGeoLocation() {
        if ( lat != null && lng != null ) return new Double[]{ lat, lng };
        return new Double[]{ 0.0, 0.0 };
    }

    public record AddressDTO(
            String street,
            String number,
            String city,
            String postalCode,
            String state,
            String country
    ) {

    }

    @Schema(
            description = "Full address object",
            implementation = AddressDTO.class
    )
    public AddressDTO getAddress() {
        return new AddressDTO(
                street,
                number,
                city,
                postalCode,
                state,
                country
        );
    }
}
