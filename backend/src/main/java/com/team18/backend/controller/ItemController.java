package com.team18.backend.controller;

import com.team18.backend.model.Item;
import com.team18.backend.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController( ItemService itemService ) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> findAllItems() {
        return itemService.findAllItems();
    }

    @GetMapping("/{id}")
    public Item findItemById( @PathVariable String id ) {
        return itemService.findItemById( id );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem( @RequestBody Item item ) {
        return itemService.createItem( item );
    }

    @PutMapping("/{id}")
    public Item updateItemById( @PathVariable String id, @RequestBody Item item ) {
        return itemService.updateItemById( id, item );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteItemById( @PathVariable String id ) {
        return itemService.deleteItemById( id );
    }
}
