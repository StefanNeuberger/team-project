package com.team18.backend.service;

import com.team18.backend.dto.ItemDTO;
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
        return itemRepo.findById( id ).orElseThrow( () -> new RuntimeException( "Item not found: " + id ) );
    }

    public Item createItem( ItemDTO item ) {
        Item newItem = new Item( null, item.getSku(), item.getName() );
        return itemRepo.save( newItem );
    }

    public Item updateItemById( String id, Item item ) {
        Item existingItem = itemRepo.findById( id ).orElseThrow( () -> new RuntimeException( "Item not found: " + id ) );

        if ( item.getName() != null ) {
            existingItem.setName( item.getName() );
        }

        if ( item.getSku() != null ) {
            existingItem.setSku( item.getSku() );
        }

        return itemRepo.save( existingItem );
    }

    public boolean deleteItemById( String id ) {
        Item existingItem = itemRepo.findById( id ).orElseThrow( () -> new RuntimeException( "Item not found: " + id ) );
        itemRepo.delete( existingItem );
        return true;
    }

    public boolean deleteAllItems() {
        itemRepo.deleteAll();
        return true;
    }
}
