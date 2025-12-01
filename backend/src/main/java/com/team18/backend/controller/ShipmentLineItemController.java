package com.team18.backend.controller;

import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.service.ShipmentLineItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @GetMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ShipmentLineItemResponseDTO>> getAllInventory( @PathVariable String id ) {
        return ResponseEntity.ok( service.getShipmentLineItemsByShipmentId( id ) );
    }
}
