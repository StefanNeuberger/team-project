package com.team18.backend.service;

import com.team18.backend.dto.warehouse.WarehouseCreateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.dto.warehouse.WarehouseUpdateDTO;
import com.team18.backend.exception.WarehouseNotFoundException;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;

    private final WarehouseMapper mapper;

    public WarehouseService( WarehouseRepository repository, WarehouseMapper mapper ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<WarehouseResponseDTO> getAllWarehouses() {
        List<Warehouse> warehouses = repository.findAll();
        return mapper.toWarehouseResponseDTOList( warehouses );
    }

    public WarehouseResponseDTO getWarehouseById( String id ) throws WarehouseNotFoundException {
        Warehouse warehouse = repository.findById( id ).orElseThrow( () -> new WarehouseNotFoundException( id ) );
        return mapper.toWarehouseResponseDTO( warehouse );
    }

    public WarehouseResponseDTO createWarehouse( WarehouseCreateDTO warehouse ) {
        Warehouse newWarehouse = mapper.toWarehouse( warehouse );
        Warehouse insertedWarehouse = repository.insert( newWarehouse );
        return mapper.toWarehouseResponseDTO( insertedWarehouse );
    }

    public WarehouseResponseDTO updateWarehouse( String id, WarehouseUpdateDTO warehouse ) throws WarehouseNotFoundException {
        Warehouse current = repository.findById( id )
                .orElseThrow(
                        () -> new WarehouseNotFoundException( id )
                );

        Warehouse updatedWarehouse = mapper.toWarehouse( current, warehouse );
        Warehouse savedWarehouse = repository.save( updatedWarehouse );
        return mapper.toWarehouseResponseDTO( savedWarehouse );
    }

    @Transactional
    public Boolean deleteWarehouse( String id ) throws WarehouseNotFoundException {
        Warehouse toDelete = repository.findById( id )
                .orElseThrow( () -> new WarehouseNotFoundException( id ) );

        repository.delete( toDelete );

        return true;
    }

}
