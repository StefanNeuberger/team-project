package com.team18.backend.controller;

import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.FieldValidationErrorResponse;
import com.team18.backend.model.Shop;
import com.team18.backend.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema; 
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@Tag(name = "Shops", description = "CRUD operations for shops")
@Validated
public class ShopController {

    private final ShopService shopService;

    public ShopController( ShopService shopService ) {
        this.shopService = shopService;
    }

    @GetMapping
    @Operation(
            summary = "Get all shops",
            description = "Returns every shop currently stored in the system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of shops",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = Shop.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<List<Shop>> getAllShops() {
        return ResponseEntity.ok( shopService.getAllShops() );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get shop by ID",
            description = "Returns a single shop by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shop found",
                    content = @Content(
                            schema = @Schema(implementation = Shop.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shop not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Shop> getShopById( @PathVariable String id ) {
        return ResponseEntity.ok( shopService.getShopById( id ) );
    }

    @PostMapping
    @Operation(
            summary = "Create a shop",
            description = "Creates a new shop with the provided name."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shop created",
                    content = @Content(schema = @Schema(implementation = Shop.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate key error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Shop> createShop(
            @Valid @RequestBody CreateShopRequest request
    ) {
        Shop createdShop = shopService.createShop( request.name() );
        return ResponseEntity.status( HttpStatus.CREATED ).body( createdShop );
    }

    private record CreateShopRequest( @NotBlank String name ) {
    }
}

