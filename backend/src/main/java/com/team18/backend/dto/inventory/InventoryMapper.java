package com.team18.backend.dto.inventory;

import com.team18.backend.model.Inventory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryMapper {

    public InventoryResponseDTO toInventoryResponseDTO( Inventory inventory ) {
        return new InventoryResponseDTO(
                inventory.getId(),
                inventory.getQuantity(),
                inventory.getCreatedDate(),
                inventory.getLastModifiedDate()
        );
    }

    public List<InventoryResponseDTO> toInventoryResponseDTOList( List<Inventory> inventoryList ) {
        return inventoryList.stream().map( this::toInventoryResponseDTO ).toList();
    }

    public Inventory toInventory( InventoryCreateDTO inventoryCreateDTO ) {
        return new Inventory(
                inventoryCreateDTO.quantity()
        );
    }

    public Inventory toInventory( Inventory inventory, InventoryUpdateDTO inventoryUpdateDTO ) {
        return new Inventory(
                inventory.getId(),
                inventoryUpdateDTO.quantity() != null ? inventoryUpdateDTO.quantity() : inventory.getQuantity(),
                inventory.getCreatedDate(),
                inventory.getLastModifiedDate()
        );
    }
}
