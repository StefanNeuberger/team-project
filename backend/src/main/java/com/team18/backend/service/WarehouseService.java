package com.team18.backend.service;

import com.team18.backend.dto.warehouse.WarehouseCreateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.dto.warehouse.WarehouseUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Inventory;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.InventoryRepository;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;
    private final ShopRepository shopRepository;
    private final InventoryRepository inventoryRepository;
    private final WarehouseMapper mapper;

    public WarehouseService(
            WarehouseRepository repository,
            ShopRepository shopRepository,
            InventoryRepository inventoryRepository,
            WarehouseMapper mapper
    ) {
        this.repository = repository;
        this.shopRepository = shopRepository;
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    public List<WarehouseResponseDTO> getAllWarehouses() {
        List<Warehouse> warehouses = repository.findAll();
        return mapper.toWarehouseResponseDTOList( warehouses );
    }

    public WarehouseResponseDTO getWarehouseById( String id ) throws ResourceNotFoundException {
        Warehouse warehouse = repository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find Warehouse with id: " + id )
        );
        return mapper.toWarehouseResponseDTO( warehouse );
    }

    public WarehouseResponseDTO createWarehouse( WarehouseCreateDTO warehouse ) {
        Shop shop = shopRepository.findById( warehouse.shopId() ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shop with id: " + warehouse.shopId() )
        );

        Warehouse newWarehouse = mapper.toWarehouse( warehouse, shop );
        Warehouse insertedWarehouse = repository.insert( newWarehouse );
        return mapper.toWarehouseResponseDTO( insertedWarehouse );
    }

    public WarehouseResponseDTO updateWarehouse( String id, WarehouseUpdateDTO warehouse ) throws ResourceNotFoundException {
        Warehouse current = repository.findById( id )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find Warehouse with id: " + id )
                );

        String shopId = warehouse.shopId();
        Shop shop = shopId != null ? shopRepository.findById( warehouse.shopId() ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shop with id: " + warehouse.shopId() )
        ) : null;

        Warehouse updatedWarehouse = mapper.toWarehouse( current, warehouse, shop );
        Warehouse savedWarehouse = repository.save( updatedWarehouse );
        return mapper.toWarehouseResponseDTO( savedWarehouse );
    }

    @Transactional
    public Boolean deleteWarehouse( String id ) throws ResourceNotFoundException {
        Warehouse toDelete = repository.findById( id )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Could not find Warehouse with id: " + id )
                );

        List<Inventory> relatedInventories = inventoryRepository
                .findAllByWarehouse_Id( toDelete.getId() );

        inventoryRepository.deleteAll( relatedInventories );

        repository.delete( toDelete );

        return true;
    }

}
