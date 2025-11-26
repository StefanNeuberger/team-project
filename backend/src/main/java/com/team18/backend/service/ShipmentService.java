package com.team18.backend.service;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
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

    public ShipmentService(
            ShipmentRepository repository,
            WarehouseRepository warehouseRepository,
            ShopRepository shopRepository
    ) {
        this.repository = repository;
        this.warehouseRepository = warehouseRepository;
        this.shopRepository = shopRepository;
    }

    public List<ShipmentResponseDTO> getAllShipments() {
        return repository.findAll().stream()
                .map(ShipmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO getShipmentById(String id) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        return ShipmentResponseDTO.fromEntity(shipment);
    }

    public List<ShipmentResponseDTO> getShipmentsByWarehouseId(String warehouseId) {
        return repository.findByWarehouseId(warehouseId).stream()
                .map(ShipmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ShipmentResponseDTO> getAllShipmentsByShopId(String shopId) {
        // Find the shop
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));
        
        // Find all warehouses for this shop
        List<Warehouse> warehouses = warehouseRepository.findByShop(shop);
        
        // Extract warehouse IDs
        List<String> warehouseIds = warehouses.stream()
                .map(Warehouse::getId)
                .collect(Collectors.toList());
        
        // Find all shipments for those warehouses
        return repository.findAllByWarehouseIdIn(warehouseIds).stream()
                .map(ShipmentResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ShipmentResponseDTO createShipment(ShipmentCreateDTO dto) {
        Shipment shipment = new Shipment(
                dto.warehouseId(),
                dto.expectedArrivalDate(),
                dto.status()
        );
        Shipment saved = repository.save(shipment);
        return ShipmentResponseDTO.fromEntity(saved);
    }

    public ShipmentResponseDTO updateShipment(String id, ShipmentUpdateDTO dto) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        
        if (dto.expectedArrivalDate() != null) {
            shipment.setExpectedArrivalDate(dto.expectedArrivalDate());
        }
        if (dto.status() != null) {
            shipment.setStatus(dto.status());
        }
        
        Shipment updated = repository.save(shipment);
        return ShipmentResponseDTO.fromEntity(updated);
    }

    public ShipmentResponseDTO updateShipmentStatus(String id, ShipmentStatusUpdateDTO dto) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
        
        shipment.setStatus(dto.status());
        Shipment updated = repository.save(shipment);
        return ShipmentResponseDTO.fromEntity(updated);
    }

    public void deleteShipment(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Shipment not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

