package com.team18.backend.dto.inventory;

import com.team18.backend.model.Inventory;
import com.team18.backend.model.Item;
import com.team18.backend.model.Warehouse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryMapper {

    public InventoryResponseDTO toInventoryResponseDTO( Inventory inventory ) {
        return new InventoryResponseDTO(
                inventory.getId(),
                inventory.getWarehouse(),
                inventory.getItem(),
                inventory.getQuantity(),
                inventory.getCreatedDate(),
                inventory.getLastModifiedDate()
        );
    }

    public List<InventoryResponseDTO> toInventoryResponseDTOList( List<Inventory> inventoryList ) {
        return inventoryList.stream().map( this::toInventoryResponseDTO ).toList();
    }

    public Inventory toInventory( InventoryCreateDTO inventoryCreateDTO, Warehouse warehouse, Item item ) {
        return new Inventory(
                warehouse,
                item,
                inventoryCreateDTO.quantity()
        );
    }

    public Inventory toInventory( Inventory inventory, InventoryUpdateDTO inventoryUpdateDTO, Warehouse warehouse, Item item ) {
        Inventory mappedInventory = new Inventory(
                inventory.getId(),
                warehouse != null ? warehouse : inventory.getWarehouse(),
                item != null ? item : inventory.getItem(),
                inventoryUpdateDTO.quantity() != null ? inventoryUpdateDTO.quantity() : inventory.getQuantity()
        );

        mappedInventory.setCreatedDate( inventory.getCreatedDate() );
        mappedInventory.setLastModifiedDate( inventory.getLastModifiedDate() );

        return mappedInventory;

    }
}
