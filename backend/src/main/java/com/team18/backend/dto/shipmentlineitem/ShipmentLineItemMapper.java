package com.team18.backend.dto.shipmentlineitem;

import com.team18.backend.model.Item;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentLineItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentLineItemMapper {

    public ShipmentLineItemResponseDTO toResponseDTO( ShipmentLineItem shipmentLineItem ) {
        return new ShipmentLineItemResponseDTO(
                shipmentLineItem.getId(),
                shipmentLineItem.getShipment(),
                shipmentLineItem.getItem(),
                shipmentLineItem.getExpectedQuantity(),
                shipmentLineItem.getReceivedQuantity(),
                shipmentLineItem.getCreatedDate(),
                shipmentLineItem.getLastModifiedDate()
        );
    }

    public List<ShipmentLineItemResponseDTO> toResponseDTOList( List<ShipmentLineItem> shipmentLineItems ) {
        return shipmentLineItems.stream().map( this::toResponseDTO ).toList();
    }

    public ShipmentLineItem toShipmentLineItem( ShipmentLineItemCreateDTO shipmentLineItemCreateDTO, Shipment shipment, Item item ) {
        return new ShipmentLineItem(
                shipment,
                item,
                shipmentLineItemCreateDTO.expectedQuantity(),
                shipmentLineItemCreateDTO.receivedQuantity()
        );
    }

    public ShipmentLineItem toShipmentLineItem( ShipmentLineItem current, ShipmentLineItemUpdateDTO shipmentLineItemUpdateDTO, Shipment shipment, Item item ) {
        current.setShipment( shipment );
        current.setItem( item );

        if ( shipmentLineItemUpdateDTO.expectedQuantity() != null ) {
            current.setExpectedQuantity( shipmentLineItemUpdateDTO.expectedQuantity() );
        }

        if ( shipmentLineItemUpdateDTO.receivedQuantity() != null ) {
            current.setReceivedQuantity( shipmentLineItemUpdateDTO.receivedQuantity() );
        }

        return current;
    }
}
