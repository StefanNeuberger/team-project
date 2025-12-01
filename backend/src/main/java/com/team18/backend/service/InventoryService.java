package com.team18.backend.service;

import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.inventory.InventoryMapper;
import com.team18.backend.dto.inventory.InventoryResponseDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Inventory;
import com.team18.backend.model.Item;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.InventoryRepository;
import com.team18.backend.repository.ItemRepo;
import com.team18.backend.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;
    private final WarehouseRepository warehouseRepository;
    private final ItemRepo itemRepo;

    public InventoryService( InventoryRepository repository, InventoryMapper mapper, WarehouseRepository warehouseRepository, ItemRepo itemRepo ) {
        this.repository = repository;
        this.mapper = mapper;
        this.warehouseRepository = warehouseRepository;
        this.itemRepo = itemRepo;
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

    public List<InventoryResponseDTO> getInventoryByItemId( String itemId ) {
        Optional<List<Inventory>> inventory = repository.findAllByItem_Id( itemId );

        if ( inventory.isEmpty() || inventory.get().isEmpty() ) {
            throw new ResourceNotFoundException( "Could not find inventory with itemId: " + itemId );
        }

        return mapper.toInventoryResponseDTOList( inventory.get() );
    }

    public List<InventoryResponseDTO> getInventoryByWarehouseId( String warehouseId ) {
        Optional<List<Inventory>> inventory = repository.findAllByWarehouse_Id( warehouseId );

        if ( inventory.isEmpty() || inventory.get().isEmpty() ) {
            throw new ResourceNotFoundException( "Could not find inventory with warehouseId: " + warehouseId );
        }

        return mapper.toInventoryResponseDTOList( inventory.get() );
    }

    public InventoryResponseDTO createInventory( InventoryCreateDTO inventoryCreateDTO ) throws ResourceNotFoundException {
        Warehouse warehouse = warehouseRepository.findById( inventoryCreateDTO.warehouseId() )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find warehouse with id: " + inventoryCreateDTO.warehouseId() )
                );
        Item item = itemRepo.findById( inventoryCreateDTO.itemId() )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find item with id: " + inventoryCreateDTO.itemId() )
                );

        Inventory newInventory = mapper.toInventory( inventoryCreateDTO, warehouse, item );
        Inventory inventory = repository.insert( newInventory );
        return mapper.toInventoryResponseDTO( inventory );
    }

    public InventoryResponseDTO updateInventory( String id, InventoryUpdateDTO inventoryUpdateDTO ) throws ResourceNotFoundException {
        Inventory current = repository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find inventory with id: " + id )
        );
        String warehouseId = inventoryUpdateDTO.warehouseId();
        String itemId = inventoryUpdateDTO.itemId();

        Warehouse warehouse = warehouseId != null ? warehouseRepository.findById( inventoryUpdateDTO.warehouseId() )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find warehouse with id: " + inventoryUpdateDTO.warehouseId() )
                ) : null;

        Item item = itemId != null ? itemRepo.findById( inventoryUpdateDTO.itemId() )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find item with id: " + inventoryUpdateDTO.itemId() )
                ) : null;


        Inventory updatedInventory = mapper.toInventory( current, inventoryUpdateDTO, warehouse, item );
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
