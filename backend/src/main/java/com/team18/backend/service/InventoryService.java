package com.team18.backend.service;

import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.inventory.InventoryMapper;
import com.team18.backend.dto.inventory.InventoryResponseDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Inventory;
import com.team18.backend.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    public InventoryService( InventoryRepository repository, InventoryMapper mapper ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<InventoryResponseDTO> getAllInventory() {
        List<Inventory> inventoryList = repository.findAll();
        return mapper.toInventoryResponseDTOList( inventoryList );
    }

    public InventoryResponseDTO getInventoryById( String id ) throws ResourceNotFoundException {
        Inventory inventory = repository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find inventory with id: " + id )
        );
        return mapper.toInventoryResponseDTO( inventory );
    }

    public InventoryResponseDTO createInventory( InventoryCreateDTO inventoryCreateDTO ) {
        Inventory newInventory = mapper.toInventory( inventoryCreateDTO );
        Inventory inventory = repository.insert( newInventory );
        return mapper.toInventoryResponseDTO( inventory );
    }

    public InventoryResponseDTO updateInventory( String id, InventoryUpdateDTO inventoryUpdateDTO ) throws ResourceNotFoundException {
        Inventory current = repository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find inventory with id: " + id )
        );

        Inventory updatedInventory = mapper.toInventory( current, inventoryUpdateDTO );
        Inventory savedInventory = repository.save( updatedInventory );

        return mapper.toInventoryResponseDTO( savedInventory );
    }

    @Transactional
    public Boolean deleteInventory( String id ) throws ResourceNotFoundException {
        Inventory current = repository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find inventory with id: " + id )
        );

        repository.delete( current );
        return true;
    }
}
