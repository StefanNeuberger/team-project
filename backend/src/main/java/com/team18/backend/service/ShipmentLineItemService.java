package com.team18.backend.service;

import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemMapper;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.ShipmentLineItem;
import com.team18.backend.repository.ShipmentLineItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentLineItemService {

    private final ShipmentLineItemMapper shipmentLineItemMapper;
    private final ShipmentLineItemRepository shipmentLineItemRepository;

    public ShipmentLineItemService(
            ShipmentLineItemRepository shipmentLineItemRepository,
            ShipmentLineItemMapper shipmentLineItemMapper
    ) {
        this.shipmentLineItemRepository = shipmentLineItemRepository;
        this.shipmentLineItemMapper = shipmentLineItemMapper;
    }

    public List<ShipmentLineItemResponseDTO> getShipmentLineItemsByShipmentId( String id ) {
        Optional<List<ShipmentLineItem>> shipmentLineItems = this.shipmentLineItemRepository
                .findAllByShipment_Id( id );

        if ( shipmentLineItems.isEmpty() || shipmentLineItems.get().isEmpty() ) {
            throw new ResourceNotFoundException( "Could not find any related ShipmentLineItems with shipment id: " + id );
        }

        return shipmentLineItemMapper.toResponseDTOList( shipmentLineItems.get() );
    }
}
