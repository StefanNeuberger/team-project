package com.team18.backend.controller;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.FieldValidationErrorResponse;
import com.team18.backend.service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
@Tag(name = "Shipments", description = "CRUD operations for shipments")
@Validated
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController( ShipmentService shipmentService ) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    @Operation(
            summary = "Get all shipments",
            description = "Returns every shipment currently stored in the system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of shipments",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ShipmentResponseDTO.class))
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
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipments() {
        return ResponseEntity.ok( shipmentService.getAllShipments() );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get shipment by ID",
            description = "Returns a single shipment by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipment found",
                    content = @Content(
                            schema = @Schema(implementation = ShipmentResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shipment not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<ShipmentResponseDTO> getShipmentById( @PathVariable String id ) {
        return ResponseEntity.ok( shipmentService.getShipmentById( id ) );
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(
            summary = "Get shipments by warehouse ID",
            description = "Returns all shipments for a specific warehouse."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved shipments for warehouse",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ShipmentResponseDTO.class))
                    )
            )
    })
    public ResponseEntity<List<ShipmentResponseDTO>> getShipmentsByWarehouseId( @PathVariable String warehouseId ) {
        return ResponseEntity.ok( shipmentService.getShipmentsByWarehouseId( warehouseId ) );
    }

    @GetMapping("/shop/{shopId}")
    @Operation(
            summary = "Get all shipments for a shop",
            description = "Returns all shipments for warehouses belonging to a shop."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved shipments for shop",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = ShipmentResponseDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shop not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ShipmentResponseDTO>> getAllShipmentsByShopId(
            @PathVariable String shopId
    ) {
        return ResponseEntity.ok( shipmentService.getAllShipmentsByShopId( shopId ) );
    }

    @PostMapping
    @Operation(
            summary = "Create a shipment",
            description = "Creates a new shipment with the provided details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Shipment created",
                    content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))
            )
    })
    public ResponseEntity<ShipmentResponseDTO> createShipment( @Valid @RequestBody ShipmentCreateDTO request ) {
        ShipmentResponseDTO createdShipment = shipmentService.createShipment( request );
        return ResponseEntity.status( HttpStatus.CREATED ).body( createdShipment );
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update a shipment",
            description = "Updates an existing shipment's details."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipment updated",
                    content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shipment not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ShipmentResponseDTO> updateShipment(
            @PathVariable String id,
            @Valid @RequestBody ShipmentUpdateDTO request
    ) {
        ShipmentResponseDTO updatedShipment = shipmentService.updateShipment( id, request );
        return ResponseEntity.ok( updatedShipment );
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Update shipment status",
            description = "Updates only the status of an existing shipment."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Shipment status updated",
                    content = @Content(schema = @Schema(implementation = ShipmentResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shipment not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ShipmentResponseDTO> updateShipmentStatus(
            @PathVariable String id,
            @Valid @RequestBody ShipmentStatusUpdateDTO request
    ) {
        ShipmentResponseDTO updatedShipment = shipmentService.updateShipmentStatus( id, request );
        return ResponseEntity.ok( updatedShipment );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a shipment",
            description = "Deletes a shipment by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Shipment deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Shipment not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deleteShipment( @PathVariable String id ) {
        shipmentService.deleteShipment( id );
        return ResponseEntity.noContent().build();
    }

}

