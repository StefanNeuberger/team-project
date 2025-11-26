package com.team18.backend.dto;

import com.team18.backend.model.Warehouse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseMapper {

    public WarehouseResponseDTO toWarehouseResponseDTO( Warehouse warehouse ) {
        return new WarehouseResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLat(),
                warehouse.getLng(),
                warehouse.getStreet(),
                warehouse.getNumber(),
                warehouse.getCity(),
                warehouse.getPostalCode(),
                warehouse.getState(),
                warehouse.getCountry(),
                warehouse.getMaxCapacity()
        );
    }

    public List<WarehouseResponseDTO> toWarehouseResponseDTOList( List<Warehouse> warehouses ) {
        return warehouses.stream().map( this::toWarehouseResponseDTO ).toList();
    }

    public Warehouse toWarehouse( WarehouseCreateDTO warehouseCreateDTO ) {
        return new Warehouse(
                warehouseCreateDTO.name(),
                warehouseCreateDTO.lat(),
                warehouseCreateDTO.lng(),
                warehouseCreateDTO.street(),
                warehouseCreateDTO.number(),
                warehouseCreateDTO.city(),
                warehouseCreateDTO.postalCode(),
                warehouseCreateDTO.state(),
                warehouseCreateDTO.country(),
                warehouseCreateDTO.maxCapacity()
        );
    }

    public Warehouse toWarehouse( Warehouse warehouse, WarehouseUpdateDTO warehouseUpdateDTO ) {
        return new Warehouse(
                warehouse.getId(),
                warehouseUpdateDTO.name() != null ? warehouseUpdateDTO.name() : warehouse.getName(),
                warehouseUpdateDTO.lat() != null ? warehouseUpdateDTO.lat() : warehouse.getLat(),
                warehouseUpdateDTO.lng() != null ? warehouseUpdateDTO.lng() : warehouse.getLng(),
                warehouseUpdateDTO.street() != null ? warehouseUpdateDTO.street() : warehouse.getStreet(),
                warehouseUpdateDTO.number() != null ? warehouseUpdateDTO.number() : warehouse.getNumber(),
                warehouseUpdateDTO.city() != null ? warehouseUpdateDTO.city() : warehouse.getCity(),
                warehouseUpdateDTO.postalCode() != null ? warehouseUpdateDTO.postalCode() : warehouse.getPostalCode(),
                warehouseUpdateDTO.state() != null ? warehouseUpdateDTO.state() : warehouse.getState(),
                warehouseUpdateDTO.country() != null ? warehouseUpdateDTO.country() : warehouse.getCountry(),
                warehouseUpdateDTO.maxCapacity() != null ? warehouseUpdateDTO.maxCapacity() : warehouse.getMaxCapacity()
        );
    }
}
