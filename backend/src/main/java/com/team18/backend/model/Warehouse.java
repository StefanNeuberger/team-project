package com.team18.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(value = "warehouses")
public class Warehouse extends BaseModel {

    // TODO:

    // @DocumentReference
    // private final Shop shop;

    @Indexed(unique = true)
    @TextIndexed
    @Schema(
            description = "Warehouse name",
            example = "Warehouse EU East",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minLength = 1
    )
    private String name;

    @Schema(
            description = "Latitude of the warehouse location",
            example = "12.43424",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private Double lat;

    @Schema(
            description = "Longitude of the warehouse location",
            example = "23.43424",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private Double lng;

    @Schema(
            description = "Street name",
            example = "Sonnenallee",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String street;

    @Schema(
            description = "Number of the street",
            example = "202",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String number;

    @Schema(
            description = "City name",
            example = "Berlin",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String city;

    @Schema(
            description = "Postal code",
            example = "13357",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String postalCode;

    @Schema(
            description = "State name",
            example = "Berlin",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String state;

    @Schema(
            description = "Country name",
            example = "Germany",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true
    )
    private String country;

    @Schema(
            description = "Maximum capacity of a warehouse",
            example = "223002",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false,
            minimum = "1"
    )
    private int maxCapacity;


    @PersistenceCreator
    public Warehouse(
            String id,
            String name,
            Double lat,
            Double lng,
            String street,
            String number,
            String city,
            String postalCode,
            String state,
            String country,
            int maxCapacity,
            Instant createdDate,
            Instant lastModifiedDate
    ) {
        super( id, createdDate, lastModifiedDate );
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.state = state;
        this.country = country;
        this.maxCapacity = maxCapacity;
    }

    public Warehouse() {
        super( null );
    }

    public Warehouse(
            String name,
            Double lat,
            Double lng,
            String street,
            String number,
            String city,
            String postalCode,
            String state,
            String country,
            int maxCapacity
    ) {
        super( null );
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.state = state;
        this.country = country;
        this.maxCapacity = maxCapacity;
    }

    public Warehouse(
            String id,
            String name,
            Double lat,
            Double lng,
            String street,
            String number,
            String city,
            String postalCode,
            String state,
            String country,
            int maxCapacity
    ) {
        super( id );
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.street = street;
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.state = state;
        this.country = country;
        this.maxCapacity = maxCapacity;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getName() {
        return name;
    }
}
