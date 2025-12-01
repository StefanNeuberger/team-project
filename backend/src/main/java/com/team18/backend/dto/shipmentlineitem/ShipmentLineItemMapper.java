package com.team18.backend.dto.shipmentlineitem;

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
                shipmentLineItem.getExpectedQuantity(),
                shipmentLineItem.getCreatedDate(),
                shipmentLineItem.getLastModifiedDate()
        );
    }

    public List<ShipmentLineItemResponseDTO> toResponseDTOList( List<ShipmentLineItem> shipmentLineItems ) {
        return shipmentLineItems.stream().map( this::toResponseDTO ).toList();
    }
}
