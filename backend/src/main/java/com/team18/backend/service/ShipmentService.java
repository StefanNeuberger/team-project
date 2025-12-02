package com.team18.backend.service;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.exception.RecordIsLockedException;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.*;
import com.team18.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final ShopRepository shopRepository;
    private final WarehouseMapper warehouseMapper;
    private final ShipmentLineItemRepository shipmentLineItemRepository;
    private final InventoryRepository inventoryRepository;

    public ShipmentService(
            ShipmentRepository repository,
            WarehouseRepository warehouseRepository,
            ShopRepository shopRepository,
            WarehouseMapper warehouseMapper,
            ShipmentLineItemRepository shipmentLineItemRepository,
            InventoryRepository inventoryRepository
    ) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.shopRepository = shopRepository;
        this.warehouseMapper = warehouseMapper;
        this.shipmentLineItemRepository = shipmentLineItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    private ShipmentResponseDTO toDTO( Shipment shipment ) {
        return new ShipmentResponseDTO(
                shipment.getId(),
                warehouseMapper.toWarehouseResponseDTO( shipment.getWarehouse() ),
                shipment.getExpectedArrivalDate(),
                shipment.getStatus(),
                shipment.getCreatedDate(),
                shipment.getLastModifiedDate()
        );
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        return repository.findAll().stream()
                .map( this::toDTO )
                .collect( Collectors.toList() );
    }

    public ShipmentResponseDTO getShipmentById( String id ) {
        Shipment shipment = repository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Shipment not found with id: " + id ) );
        return toDTO( shipment );
    }

    public List<ShipmentResponseDTO> getShipmentsByWarehouseId( String warehouseId ) {
        Warehouse warehouse = warehouseRepository.findById( warehouseId )
                .orElseThrow( () -> new ResourceNotFoundException( "Warehouse not found with id: " + warehouseId ) );
        return repository.findByWarehouse( warehouse ).stream()
                .map( this::toDTO )
                .collect( Collectors.toList() );
    }

    public List<ShipmentResponseDTO> getAllShipmentsByShopId( String shopId ) {
        // Find the shop
        Shop shop = shopRepository.findById( shopId )
                .orElseThrow( () -> new ResourceNotFoundException( "Shop not found with id: " + shopId ) );

        // Find all warehouses for this shop
        List<Warehouse> warehouses = warehouseRepository.findByShop( shop );

        // Find all shipments for those warehouses
        return repository.findAllByWarehouseIn( warehouses ).stream()
                .map( this::toDTO )
                .collect( Collectors.toList() );
    }

    public ShipmentResponseDTO createShipment( ShipmentCreateDTO dto ) {
        // Find and validate the warehouse
        Warehouse warehouse = warehouseRepository.findById( dto.warehouseId() )
                .orElseThrow( () -> new ResourceNotFoundException( "Warehouse not found with id: " + dto.warehouseId() ) );

        Shipment shipment = new Shipment(
                warehouse,
                dto.expectedArrivalDate(),
                dto.status()
        );
        Shipment saved = repository.save( shipment );
        return toDTO( saved );
    }

    public ShipmentResponseDTO updateShipment( String id, ShipmentUpdateDTO dto ) {
        Shipment shipment = repository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Shipment not found with id: " + id ) );

        if ( shipment.getStatus() == ShipmentStatus.COMPLETED ) {
            throw new RecordIsLockedException( "Cannot modify shipments with status COMPLETED" );
        }

        // Update warehouse if provided
        if ( dto.warehouseId() != null ) {
            Warehouse warehouse = warehouseRepository.findById( dto.warehouseId() )
                    .orElseThrow( () -> new ResourceNotFoundException( "Warehouse not found with id: " + dto.warehouseId() ) );
            shipment.setWarehouse( warehouse );
        }

        if ( dto.expectedArrivalDate() != null ) {
            shipment.setExpectedArrivalDate( dto.expectedArrivalDate() );
        }
        if ( dto.status() != null ) {
            shipment.setStatus( dto.status() );
        }

        Shipment updated = repository.save( shipment );
        return toDTO( updated );
    }

    @Transactional
    public ShipmentResponseDTO updateShipmentStatus( String id, ShipmentStatusUpdateDTO dto ) {
        Shipment shipment = repository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Shipment not found with id: " + id ) );

        if ( shipment.getStatus() == ShipmentStatus.COMPLETED ) {
            throw new RecordIsLockedException( "Cannot modify shipments with status completed" );
        }

        if ( dto.status() == ShipmentStatus.COMPLETED ) {
            processShipmentCompletion( shipment );
        }


        shipment.setStatus( dto.status() );
        Shipment updated = repository.save( shipment );
        return toDTO( updated );
    }

    @Transactional
    public void processShipmentCompletion( Shipment shipment ) {
        Warehouse warehouse = shipment.getWarehouse();
        List<ShipmentLineItem> lineItems = shipmentLineItemRepository.findAllByShipment_Id( shipment.getId() )
                .orElseThrow(
                        () -> new ResourceNotFoundException( "Shipment has no line items" )
                );

        List<Inventory> inventoryList = inventoryRepository.findAllByWarehouse_Id( warehouse.getId() )
                .orElse( List.of() );

        Map<String, Inventory> inventoryMap = inventoryList.stream().collect(
                Collectors.toMap( inventory -> inventory.getItem().getId(), inventory -> inventory )
        );

        List<Inventory> upsertInventoryList = new ArrayList<>();


        for ( ShipmentLineItem lineItem : lineItems ) {
            Inventory inventory = inventoryMap.get( lineItem.getItem().getId() );

            if ( inventory != null ) {
                int newQuantity = inventory.getQuantity() + lineItem.getReceivedQuantity();
                inventory.setQuantity( newQuantity );
                upsertInventoryList.add( inventory );
            } else {
                upsertInventoryList.add(
                        new Inventory(
                                warehouse,
                                lineItem.getItem(),
                                lineItem.getReceivedQuantity()
                        )
                );
            }
        }

        this.inventoryRepository.saveAll( upsertInventoryList );
    }

    public void deleteShipment( String id ) {
        Shipment shipment = repository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Shipment not found with id: " + id ) );

        if ( shipment.getStatus() == ShipmentStatus.COMPLETED ) {
            throw new RecordIsLockedException( "Cannot modify shipments with status completed" );
        }

        shipmentLineItemRepository
                .findAllByShipment_Id( id )
                .ifPresent( this.shipmentLineItemRepository::deleteAll );

        repository.deleteById( id );
    }
}

