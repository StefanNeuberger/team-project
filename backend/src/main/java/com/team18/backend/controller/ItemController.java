package com.team18.backend.controller;

import com.team18.backend.dto.ItemDTO;
import com.team18.backend.model.Item;
import com.team18.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
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
            description = "Returns every Item currently stored in the system."
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "List of Items",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            )
    )
    public ResponseEntity<List<Item>> findAllItems() {
        return ResponseEntity.ok( itemService.findAllItems() );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Item by ID",
            description = "Returns a single Item by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found",
                    content = @Content
            )
    })
    public ResponseEntity<Item> findItemById( @PathVariable String id ) {
        return ResponseEntity.ok( itemService.findItemById( id ) );
    }

    @PostMapping
    @Operation(
            summary = "Create a Item",
            description = "Creates a new Item with the provided name."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Item created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            )
    })
    public ResponseEntity<Item> createItem( @RequestBody @Valid ItemDTO item ) {
        Item createdItem = itemService.createItem( item );
        return ResponseEntity.status( 201 ).body( createdItem );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an Item",
            description = "Updates the Item with the given ID using the provided data."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found",
                    content = @Content
            )
    })
    public ResponseEntity<Item> updateItemById( @PathVariable String id, @Valid @RequestBody ItemDTO itemDTO ) {
        return ResponseEntity.ok( itemService.updateItemById( id, itemDTO ) );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an Item",
            description = "Deletes the Item with the given ID from the system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Item deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteItemById( @PathVariable String id ) {
        itemService.deleteItemById( id );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Delete all Items",
            description = "Deletes all Items from the system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "All Items deleted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Item.class)
                    )
            )
    })
    public ResponseEntity<Void> deleteAllItems() {
        itemService.deleteAllItems();
        return ResponseEntity.noContent().build();
    }
}
