package com.team18.backend.service;

import com.team18.backend.dto.ItemDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Item;
import com.team18.backend.repository.ItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepo itemRepo;

    public ItemService( ItemRepo itemRepo ) {
        this.itemRepo = itemRepo;
    }

    public List<Item> findAllItems() {
        return itemRepo.findAll();
    }

    public Item findItemById( String id ) {
        return itemRepo.findById( id ).orElseThrow( () -> new ResourceNotFoundException( "Item not found: " + id ) );
    }

    public Item createItem( ItemDTO item ) {
        Item newItem = new Item( null, item.sku(), item.name() );
        return itemRepo.save( newItem );
    }

    public Item updateItemById( String id, ItemDTO itemDTO ) {
        Item existingItem = itemRepo.findById( id ).orElseThrow( () -> new ResourceNotFoundException( "Item not found: " + id ) );

        if ( itemDTO.name() != null ) {
            existingItem.setName( itemDTO.name() );
        }

        if ( itemDTO.sku() != null ) {
            existingItem.setSku( itemDTO.sku() );
        }

        return itemRepo.save( existingItem );
    }

    public void deleteItemById( String id ) {
        Item existingItem = itemRepo.findById( id ).orElseThrow( () -> new ResourceNotFoundException( "Item not found: " + id ) );
        itemRepo.delete( existingItem );
    }

    public void deleteAllItems() {
        itemRepo.deleteAll();
    }
}
