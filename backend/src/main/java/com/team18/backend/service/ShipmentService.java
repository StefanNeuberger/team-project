package com.team18.backend.service;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.ShipmentRepository;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final WarehouseRepository warehouseRepository;
    private final ShopRepository shopRepository;
    private final WarehouseMapper warehouseMapper;

    public ShipmentService(
            ShipmentRepository repository,
            WarehouseRepository warehouseRepository,
            ShopRepository shopRepository,
            WarehouseMapper warehouseMapper
    ) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.shopRepository = shopRepository;
        this.warehouseMapper = warehouseMapper;
    }

    private ShipmentResponseDTO toDTO(Shipment shipment) {
        return new ShipmentResponseDTO(
                shipment.getId(),
                warehouseMapper.toWarehouseResponseDTO(shipment.getWarehouse()),
                shipment.getExpectedArrivalDate(),
                shipment.getStatus(),
                shipment.getCreatedDate(),
                shipment.getLastModifiedDate()
        );
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO getShipmentById(String id) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        return toDTO(shipment);
    }

    public List<ShipmentResponseDTO> getShipmentsByWarehouseId(String warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + warehouseId));
        return repository.findByWarehouse(warehouse).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponseDTO> getAllShipmentsByShopId(String shopId) {
        // Find the shop
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        // Find all warehouses for this shop
        List<Warehouse> warehouses = warehouseRepository.findByShop(shop);
        
        // Find all shipments for those warehouses
        return repository.findAllByWarehouseIn(warehouses).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO createShipment(ShipmentCreateDTO dto) {
        // Find and validate the warehouse
        Warehouse warehouse = warehouseRepository.findById(dto.warehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.warehouseId()));
        
        Shipment shipment = new Shipment(
                warehouse,
                dto.expectedArrivalDate(),
                dto.status()
        );
        Shipment saved = repository.save(shipment);
        return toDTO(saved);
    }

    public ShipmentResponseDTO updateShipment(String id, ShipmentUpdateDTO dto) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        
        // Update warehouse if provided
        if (dto.warehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.warehouseId()));
            shipment.setWarehouse(warehouse);
        }
        
        if (dto.expectedArrivalDate() != null) {
            shipment.setExpectedArrivalDate(dto.expectedArrivalDate());
        }
        if (dto.status() != null) {
            shipment.setStatus(dto.status());
        }
        
        Shipment updated = repository.save(shipment);
        return toDTO(updated);
    }

    public ShipmentResponseDTO updateShipmentStatus(String id, ShipmentStatusUpdateDTO dto) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        
        shipment.setStatus(dto.status());
        Shipment updated = repository.save(shipment);
        return toDTO(updated);
    }

    public void deleteShipment(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Shipment not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

