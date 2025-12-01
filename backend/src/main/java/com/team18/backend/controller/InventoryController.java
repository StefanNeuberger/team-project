package com.team18.backend.controller;

import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.inventory.InventoryResponseDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.service.InventoryService;
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
        path = "/api/inventory",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        headers = {
                "Content-Type=" + MediaType.APPLICATION_JSON_VALUE,
                "Accept=" + MediaType.APPLICATION_JSON_VALUE
        }
)
@Tag(name = "Inventory", description = "CRUD endpoint for inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController( InventoryService service ) {
        this.service = service;
    }

    @Operation(
            summary = "Get all inventory",
            description = "Returns a list of all saved inventory"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of inventory",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = InventoryResponseDTO.class))
            )
    )
    @GetMapping(path = "", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<InventoryResponseDTO>> getAll() {
        return ResponseEntity.ok( service.getAllInventory() );
    }

    @Operation(
            summary = "Get inventory by id",
            description = "Returns a single Inventory by its id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Inventory found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InventoryResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Inventory not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @GetMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<InventoryResponseDTO> get( @PathVariable String id ) throws ResourceNotFoundException {
        return ResponseEntity.ok( service.getInventoryById( id ) );
    }

    @Operation(
            summary = "Create Inventory",
            description = "Creates a new inventory and returns the saved entity"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Inventory created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InventoryResponseDTO.class)
            )
    )
    @PostMapping(path = "")
    public ResponseEntity<InventoryResponseDTO> post( @Valid @RequestBody InventoryCreateDTO inventoryCreateDTO ) {
        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( service.createInventory( inventoryCreateDTO ) );
    }

    @Operation(
            summary = "Update Inventory",
            description = "Updates a inventory and returns the updated entity"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Inventory updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InventoryResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Inventory not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @PatchMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> patch(
            @PathVariable String id,
            @Valid @RequestBody InventoryUpdateDTO inventoryUpdateDTO
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok().body( service.updateInventory( id, inventoryUpdateDTO ) );
    }

    @Operation(
            summary = "Delete Inventory",
            description = "Deletes a inventory by its id and returns true"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Inventory deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Inventory not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @DeleteMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Boolean> delete( @PathVariable String id ) throws ResourceNotFoundException {
        return ResponseEntity.status( HttpStatus.NO_CONTENT ).body( service.deleteInventory( id ) );
    }
}
