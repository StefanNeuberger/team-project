package com.team18.backend.controller;

import com.team18.backend.dto.warehouse.WarehouseCreateDTO;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.dto.warehouse.WarehouseUpdateDTO;
import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.service.WarehouseService;
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
        path = "/api/warehouses",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE,
        headers = {
                "Content-Type=" + MediaType.APPLICATION_JSON_VALUE,
                "Accept=" + MediaType.APPLICATION_JSON_VALUE
        }
)
@Tag(name = "Warehouses", description = "CRUD endpoint for warehouses")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController( WarehouseService service ) {
        this.service = service;
    }

    @Operation(
            summary = "Get all warehouses",
            description = "Returns a list of all saved warehouses"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of Warehouse",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = WarehouseResponseDTO.class))
            )
    )
    @GetMapping(path = "", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<WarehouseResponseDTO>> getAllWarehouses() {
        return ResponseEntity.ok( service.getAllWarehouses() );
    }

    @Operation(
            summary = "Get warehouse by id",
            description = "Returns a single Warehouse by its id"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = WarehouseResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Warehouse not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @GetMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<WarehouseResponseDTO> getWarehouse( @PathVariable String id ) throws ResourceNotFoundException {
        return ResponseEntity.ok( service.getWarehouseById( id ) );
    }

    @Operation(
            summary = "Create Warehouse",
            description = "Creates a new warehouse and returns the saved entity"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Warehouse created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = WarehouseResponseDTO.class)
            )
    )
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<WarehouseResponseDTO> postWarehouse( @Valid @RequestBody WarehouseCreateDTO warehouse ) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( service.createWarehouse( warehouse ) );
    }

    @Operation(
            summary = "Update Warehouse",
            description = "Updates a warehouse and returns the updated entity"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Warehouse updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = WarehouseResponseDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Warehouse not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @PatchMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> patchWarehouse( @PathVariable String id, @Valid @RequestBody WarehouseUpdateDTO warehouse )
            throws ResourceNotFoundException {
        return ResponseEntity.ok( service.updateWarehouse( id, warehouse ) );
    }

    @Operation(
            summary = "Delete Warehouse",
            description = "Deletes a warehouse by its id and returns true"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Warehouse deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Warehouse not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )

    )
    @DeleteMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean deleteWarehouse( @PathVariable String id ) throws ResourceNotFoundException {
        return service.deleteWarehouse( id );
    }
}
