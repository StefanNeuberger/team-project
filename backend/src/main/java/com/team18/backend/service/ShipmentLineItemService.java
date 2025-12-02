package com.team18.backend.service;

import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemCreateDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemMapper;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemUpdateDTO;
import com.team18.backend.exception.RecordIsLockedException;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Item;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentLineItem;
import com.team18.backend.model.ShipmentStatus;
import com.team18.backend.repository.ItemRepo;
import com.team18.backend.repository.ShipmentLineItemRepository;
import com.team18.backend.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentLineItemService {

    private final ShipmentLineItemMapper mapper;
    private final ShipmentRepository shipmentRepository;
    private final ItemRepo itemRepo;
    private final ShipmentLineItemRepository shipmentLineItemRepository;

    public ShipmentLineItemService(
            ShipmentLineItemMapper mapper,
            ShipmentRepository shipmentRepository,
            ItemRepo itemRepo,
            ShipmentLineItemRepository shipmentLineItemRepository

    ) {
        this.mapper = mapper;
        this.shipmentRepository = shipmentRepository;
        this.itemRepo = itemRepo;
        this.shipmentLineItemRepository = shipmentLineItemRepository;
    }

    public List<ShipmentLineItemResponseDTO> getShipmentLineItemsByShipmentId( String id ) {
        Optional<List<ShipmentLineItem>> shipmentLineItems = this.shipmentLineItemRepository
                .findAllByShipment_Id( id );

        if ( shipmentLineItems.isEmpty() || shipmentLineItems.get().isEmpty() ) {
            throw new ResourceNotFoundException( "Could not find any related ShipmentLineItems with shipment id: " + id );
        }

        return mapper.toResponseDTOList( shipmentLineItems.get() );
    }

    public ShipmentLineItemResponseDTO createShipmentLineItem( ShipmentLineItemCreateDTO shipmentLineItemCreateDTO ) {
        Shipment shipment = shipmentRepository.findById( shipmentLineItemCreateDTO.shipmentId() ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shipment with id: " + shipmentLineItemCreateDTO.shipmentId() )
        );

        Item item = itemRepo.findById( shipmentLineItemCreateDTO.itemId() ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find item with id: " + shipmentLineItemCreateDTO.itemId() )
        );

        ShipmentLineItem newLineItem = shipmentLineItemRepository.insert(
                mapper.toShipmentLineItem( shipmentLineItemCreateDTO, shipment, item )
        );

        return mapper.toResponseDTO( newLineItem );
    }

    public ShipmentLineItemResponseDTO updateShipmentLineItem( String id, ShipmentLineItemUpdateDTO updateDTO ) {
        ShipmentLineItem current = shipmentLineItemRepository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shipment with id: " + id )
        );

        if ( current.getShipment().getStatus() == ShipmentStatus.COMPLETED ) {
            throw new RecordIsLockedException( "Cannot modify line items with shipment status completed" );
        }

        String shipmentId = updateDTO.shipmentId();
        String itemId = updateDTO.itemId();

        Shipment shipment = shipmentId != null ? shipmentRepository.findById( shipmentId ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shipment with id: " + shipmentId )
        ) : current.getShipment();

        Item item = itemId != null ? itemRepo.findById( itemId ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find item with id: " + itemId )
        ) : current.getItem();

        ShipmentLineItem updatedLineItem = shipmentLineItemRepository.save(
                mapper.toShipmentLineItem( current, updateDTO, shipment, item )
        );
        System.out.println( updatedLineItem );

        return mapper.toResponseDTO( updatedLineItem );
    }

    @Transactional
    public Boolean deleteShipmentLineItem( String id ) {
        ShipmentLineItem current = shipmentLineItemRepository.findById( id ).orElseThrow(
                () -> new ResourceNotFoundException( "Could not find shipment with id: " + id )
        );

        if ( current.getShipment().getStatus() == ShipmentStatus.COMPLETED ) {
            throw new RecordIsLockedException( "Cannot modify line items with shipment status completed" );
        }

        this.shipmentLineItemRepository.delete( current );

        return true;
    }
}
