package com.team18.backend.controller;

import com.team18.backend.dto.item.ItemDTO;
import com.team18.backend.exception.ErrorResponse;
import com.team18.backend.exception.FieldValidationErrorResponse;
import com.team18.backend.model.Item;
import com.team18.backend.model.Shop;
import com.team18.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Items", description = "CRUD operations for items")
@Validated
public class ItemController {

    private final ItemService itemService;

    public ItemController( ItemService itemService ) {
        this.itemService = itemService;
    }


    @GetMapping
    @Operation(
            summary = "Get all Items",
            description = "Returns an array of all Items currently stored in the system or an empty array if no items are stored."
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of Items",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = Shop.class))
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )

    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok( itemService.findAllItems() );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Item by ID",
            description = "Returns a single Item by its unique identifier."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Item found",
            content = @Content(
                    schema = @Schema(implementation = Item.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )

    public ResponseEntity<Item> getItemById( @PathVariable String id ) {
        return ResponseEntity.ok( itemService.findItemById( id ) );
    }

    @PostMapping
    @Operation(
            summary = "Create a Item",
            description = "Creates a new Item with the provided name."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Item created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Item.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))
    )

    public ResponseEntity<Item> createItem( @RequestBody @Valid ItemDTO item ) {
        Item createdItem = itemService.createItem( item );
        return ResponseEntity.status( 201 ).body( createdItem );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an Item",
            description = "Updates the Item with the given ID using the provided data."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Item updated",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Item.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = FieldValidationErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )

    public ResponseEntity<Item> updateItemById( @PathVariable String id, @Valid @RequestBody ItemDTO itemDTO ) {
        return ResponseEntity.ok( itemService.updateItemById( id, itemDTO ) );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an Item",
            description = "Deletes the Item with the given ID from the system."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Item deleted"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Item not found",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )

    public ResponseEntity<Void> deleteItemById( @PathVariable String id ) {
        itemService.deleteItemById( id );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Delete all Items",
            description = "Deletes all Items from the system."
    )
    @ApiResponse(
            responseCode = "204",
            description = "All Items deleted"
    )
    public ResponseEntity<Void> deleteAllItems() {
        itemService.deleteAllItems();
        return ResponseEntity.noContent().build();
    }
}
