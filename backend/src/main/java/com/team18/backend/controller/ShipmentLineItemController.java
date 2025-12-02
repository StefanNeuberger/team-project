package com.team18.backend.controller;

import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemCreateDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemUpdateDTO;
import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.service.ShipmentLineItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/shipmentlineitems",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        headers = {
                "Content-Type=" + MediaType.APPLICATION_JSON_VALUE,
                "Accept=" + MediaType.APPLICATION_JSON_VALUE
        }
)
@Tag(name = "ShipmentLineItems", description = "CRUD operations for shipment line items")
public class ShipmentLineItemController {

    private final ShipmentLineItemService service;

    public ShipmentLineItemController( ShipmentLineItemService shipmentLineItemService ) {
        this.service = shipmentLineItemService;
    }

    @Operation(
            summary = "Get all shipment line items by shipment id",
            description = "Returns a list of all shipment line items related to a specific shipment"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of ShipmentLineItem",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ShipmentLineItemResponseDTO.class))
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Shipment line items not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @GetMapping(path = "/byShipmentId/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ShipmentLineItemResponseDTO>> getAllShipmentLineItemsByShipmentId( @PathVariable String id ) {
        return ResponseEntity.ok( service.getShipmentLineItemsByShipmentId( id ) );
    }

    @Operation(
            summary = "Create ShipmentLineItem",
            description = "Creates a new ShipmentLineItem and returns a ShipmentLineItemResponseDTO"
    )
    @ApiResponse(
            responseCode = "201",
            description = "ShipmentLineItem created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShipmentLineItemResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Shipment ot Item not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PostMapping("")
    public ResponseEntity<ShipmentLineItemResponseDTO> createShipmentLineItem(
            @Valid @RequestBody ShipmentLineItemCreateDTO shipmentLineItemCreateDTO
    ) throws ResourceNotFoundException {
        return ResponseEntity.status( HttpStatus.CREATED ).body( service.createShipmentLineItem( shipmentLineItemCreateDTO ) );
    }

    @Operation(
            summary = "Update ShipmentLineItem",
            description = "Updates a ShipmentLineItem and returns a ShipmentLineItemResponseDTO"
    )
    @ApiResponse(
            responseCode = "200",
            description = "ShipmentLineItem updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ShipmentLineItemResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "ShipmentLineItem, Shipment or Item not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Record is locked",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentLineItemResponseDTO> updateShipmentLineItem(
            @PathVariable String id,
            @Valid @RequestBody ShipmentLineItemUpdateDTO shipmentLineItemUpdateDTO
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body( service.updateShipmentLineItem( id, shipmentLineItemUpdateDTO ) );
    }

    @Operation(
            summary = "Delete ShipmentLineItem",
            description = "Deletes a ShipmentLineItem by its id and returns true"
    )
    @ApiResponse(
            responseCode = "204",
            description = "ShipmentLineItem deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "ShipmentLineItem not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Record is locked",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Boolean> deleteShipmentLineItem( @PathVariable String id ) throws ResourceNotFoundException {
        return ResponseEntity.status( HttpStatus.NO_CONTENT ).body( service.deleteShipmentLineItem( id ) );
    }
}
