package com.team18.backend.service;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentStatus;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.ShipmentRepository;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment testShipment;
    private Shop testShop;
    private Warehouse testWarehouse;

    @BeforeEach
    void setUp() {
        testShop = new Shop("shop-123", "Test Shop");
        testWarehouse = new Warehouse(
                "warehouse-1", "Test Warehouse", testShop,
                0.0, 0.0, "Street", "1", "City", "12345", "State", "Country", 100
        );
        testShipment = new Shipment(
                "shipment-123",
                testWarehouse,
                LocalDate.of(2025, 12, 1),
                ShipmentStatus.ORDERED
        );
    }

    @Test
    void getAllShipments_shouldReturnAllShipments_whenRepositoryHasShipments() {
        // GIVEN
        stubWarehouseMapper();
        Warehouse warehouse2 = new Warehouse(
                "warehouse-2", "Warehouse 2", testShop,
                0.0, 0.0, "Street", "2", "City", "12345", "State", "Country", 100
        );
        Shipment shipment1 = new Shipment(testWarehouse, LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment shipment2 = new Shipment(warehouse2, LocalDate.of(2025, 12, 5), ShipmentStatus.IN_DELIVERY);
        List<Shipment> expectedShipments = List.of(shipment1, shipment2);
        when(shipmentRepository.findAll()).thenReturn(expectedShipments);

        // WHEN
        List<ShipmentResponseDTO> actualShipments = shipmentService.getAllShipments();

        // THEN
        assertThat(actualShipments).hasSize(2);
        verify(shipmentRepository).findAll();
    }

    @Test
    void getAllShipments_shouldReturnEmptyList_whenRepositoryHasNoShipments() {
        // GIVEN
        when(shipmentRepository.findAll()).thenReturn(List.of());

        // WHEN
        List<ShipmentResponseDTO> actualShipments = shipmentService.getAllShipments();

        // THEN
        assertThat(actualShipments).isEmpty();
        verify(shipmentRepository).findAll();
    }

    @Test
    void getShipmentById_shouldReturnShipment_whenShipmentExists() {
        // GIVEN
        stubWarehouseMapper();
        String shipmentId = "shipment-123";
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(testShipment));

        // WHEN
        ShipmentResponseDTO actualShipment = shipmentService.getShipmentById(shipmentId);

        // THEN
        assertThat(actualShipment).isNotNull();
        assertThat(actualShipment.id()).isEqualTo(shipmentId);
        assertThat(actualShipment.warehouse().id()).isEqualTo("warehouse-1");
        assertThat(actualShipment.status()).isEqualTo(ShipmentStatus.ORDERED);
        verify(shipmentRepository).findById(shipmentId);
    }

    @Test
    void getShipmentById_shouldThrowResourceNotFoundException_whenShipmentDoesNotExist() {
        // GIVEN
        String shipmentId = "non-existent-id";
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> shipmentService.getShipmentById(shipmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Shipment not found with id: " + shipmentId);
        verify(shipmentRepository).findById(shipmentId);
    }

    @Test
    void getShipmentsByWarehouseId_shouldReturnShipmentsForWarehouse_whenShipmentsExist() {
        // GIVEN
        stubWarehouseMapper();
        String warehouseId = "warehouse-1";
        List<Shipment> expectedShipments = List.of(testShipment);
        when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(testWarehouse));
        when(shipmentRepository.findByWarehouse(testWarehouse)).thenReturn(expectedShipments);

        // WHEN
        List<ShipmentResponseDTO> actualShipments = shipmentService.getShipmentsByWarehouseId(warehouseId);

        // THEN
        assertThat(actualShipments).hasSize(1);
        assertThat(actualShipments.get(0).warehouse().id()).isEqualTo(warehouseId);
        verify(warehouseRepository).findById(warehouseId);
        verify(shipmentRepository).findByWarehouse(testWarehouse);
    }

    @Test
    void createShipment_shouldSaveAndReturnShipment_whenValidDataProvided() {
        // GIVEN
        stubWarehouseMapper();
        ShipmentCreateDTO dto = new ShipmentCreateDTO(
                "warehouse-1",
                LocalDate.of(2025, 12, 1),
                ShipmentStatus.ORDERED
        );
        when(warehouseRepository.findById("warehouse-1")).thenReturn(Optional.of(testWarehouse));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        // WHEN
        ShipmentResponseDTO actualShipment = shipmentService.createShipment(dto);

        // THEN
        assertThat(actualShipment).isNotNull();
        assertThat(actualShipment.warehouse().id()).isEqualTo("warehouse-1");
        assertThat(actualShipment.status()).isEqualTo(ShipmentStatus.ORDERED);
        verify(warehouseRepository).findById("warehouse-1");
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void createShipment_shouldThrowResourceNotFoundException_whenWarehouseDoesNotExist() {
        // GIVEN
        ShipmentCreateDTO dto = new ShipmentCreateDTO(
                "non-existent-warehouse",
                LocalDate.of(2025, 12, 1),
                ShipmentStatus.ORDERED
        );
        when(warehouseRepository.findById("non-existent-warehouse")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> shipmentService.createShipment(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Warehouse not found with id: non-existent-warehouse");
        verify(warehouseRepository).findById("non-existent-warehouse");
    }

    @Test
    void updateShipment_shouldUpdateAndReturnShipment_whenShipmentExists() {
        // GIVEN
        stubWarehouseMapper();
        String shipmentId = "shipment-123";
        ShipmentUpdateDTO dto = new ShipmentUpdateDTO(
                null,
                LocalDate.of(2025, 12, 15),
                ShipmentStatus.IN_DELIVERY
        );
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(testShipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        // WHEN
        ShipmentResponseDTO updatedShipment = shipmentService.updateShipment(shipmentId, dto);

        // THEN
        assertThat(updatedShipment).isNotNull();
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void updateShipmentStatus_shouldUpdateStatusAndReturnShipment_whenShipmentExists() {
        // GIVEN
        stubWarehouseMapper();
        String shipmentId = "shipment-123";
        ShipmentStatusUpdateDTO dto = new ShipmentStatusUpdateDTO(ShipmentStatus.PROCESSED);
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(testShipment));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        // WHEN
        ShipmentResponseDTO updatedShipment = shipmentService.updateShipmentStatus(shipmentId, dto);

        // THEN
        assertThat(updatedShipment).isNotNull();
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void deleteShipment_shouldDeleteShipment_whenShipmentExists() {
        // GIVEN
        String shipmentId = "shipment-123";
        when(shipmentRepository.existsById(shipmentId)).thenReturn(true);

        // WHEN
        shipmentService.deleteShipment(shipmentId);

        // THEN
        verify(shipmentRepository).existsById(shipmentId);
        verify(shipmentRepository).deleteById(shipmentId);
    }

    @Test
    void deleteShipment_shouldThrowResourceNotFoundException_whenShipmentDoesNotExist() {
        // GIVEN
        String shipmentId = "non-existent-id";
        when(shipmentRepository.existsById(shipmentId)).thenReturn(false);

        // WHEN & THEN
        assertThatThrownBy(() -> shipmentService.deleteShipment(shipmentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Shipment not found with id: " + shipmentId);
        verify(shipmentRepository).existsById(shipmentId);
    }

    @Test
    void getAllShipmentsByShopId_shouldReturnShipments_whenShopAndWarehousesExist() {
        // GIVEN
        stubWarehouseMapper();
        String shopId = "shop-123";
        Warehouse warehouse1 = new Warehouse(
                "warehouse-1", "Warehouse 1", testShop, 
                0.0, 0.0, "Street", "1", "City", "12345", "State", "Country", 100
        );
        Warehouse warehouse2 = new Warehouse(
                "warehouse-2", "Warehouse 2", testShop,
                0.0, 0.0, "Street", "2", "City", "12345", "State", "Country", 100
        );
        List<Warehouse> warehouses = List.of(warehouse1, warehouse2);
        
        Shipment shipment1 = new Shipment(warehouse1, LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment shipment2 = new Shipment(warehouse2, LocalDate.of(2025, 12, 5), ShipmentStatus.IN_DELIVERY);
        Shipment shipment3 = new Shipment(warehouse1, LocalDate.of(2025, 12, 10), ShipmentStatus.PROCESSED);
        List<Shipment> shipments = List.of(shipment1, shipment2, shipment3);

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(testShop));
        when(warehouseRepository.findByShop(testShop)).thenReturn(warehouses);
        when(shipmentRepository.findAllByWarehouseIn(warehouses))
                .thenReturn(shipments);

        // WHEN
        List<ShipmentResponseDTO> result = shipmentService.getAllShipmentsByShopId(shopId);

        // THEN
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(dto ->
                dto.warehouse().id().equals("warehouse-1") || dto.warehouse().id().equals("warehouse-2")
        );
        verify(shopRepository).findById(shopId);
        verify(warehouseRepository).findByShop(testShop);
        verify(shipmentRepository).findAllByWarehouseIn(warehouses);
    }

    @Test
    void getAllShipmentsByShopId_shouldThrowResourceNotFoundException_whenShopDoesNotExist() {
        // GIVEN
        String shopId = "non-existent-shop";
        when(shopRepository.findById(shopId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> shipmentService.getAllShipmentsByShopId(shopId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Shop not found with id: " + shopId);
        verify(shopRepository).findById(shopId);
    }

    @Test
    void getAllShipmentsByShopId_shouldReturnEmptyList_whenShopHasNoWarehouses() {
        // GIVEN
        String shopId = "shop-123";
        when(shopRepository.findById(shopId)).thenReturn(Optional.of(testShop));
        when(warehouseRepository.findByShop(testShop)).thenReturn(List.of());
        when(shipmentRepository.findAllByWarehouseIn(List.of())).thenReturn(List.of());

        // WHEN
        List<ShipmentResponseDTO> result = shipmentService.getAllShipmentsByShopId(shopId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(shopRepository).findById(shopId);
        verify(warehouseRepository).findByShop(testShop);
        verify(shipmentRepository).findAllByWarehouseIn(List.of());
    }

    @Test
    void getAllShipmentsByShopId_shouldReturnEmptyList_whenWarehousesHaveNoShipments() {
        // GIVEN
        String shopId = "shop-123";
        Warehouse warehouse1 = new Warehouse(
                "warehouse-1", "Warehouse 1", testShop, 
                0.0, 0.0, "Street", "1", "City", "12345", "State", "Country", 100
        );
        Warehouse warehouse2 = new Warehouse(
                "warehouse-2", "Warehouse 2", testShop,
                0.0, 0.0, "Street", "2", "City", "12345", "State", "Country", 100
        );
        List<Warehouse> warehouses = List.of(warehouse1, warehouse2);

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(testShop));
        when(warehouseRepository.findByShop(testShop)).thenReturn(warehouses);
        when(shipmentRepository.findAllByWarehouseIn(warehouses))
                .thenReturn(List.of());

        // WHEN
        List<ShipmentResponseDTO> result = shipmentService.getAllShipmentsByShopId(shopId);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(shopRepository).findById(shopId);
        verify(warehouseRepository).findByShop(testShop);
        verify(shipmentRepository).findAllByWarehouseIn(warehouses);
    }

    @Test
    void getAllShipmentsByShopId_shouldExtractCorrectWarehouseIds_whenMultipleWarehousesExist() {
        // GIVEN
        String shopId = "shop-123";
        Warehouse warehouse1 = new Warehouse(
                "warehouse-1", "Warehouse 1", testShop, 
                0.0, 0.0, "Street", "1", "City", "12345", "State", "Country", 100
        );
        Warehouse warehouse2 = new Warehouse(
                "warehouse-2", "Warehouse 2", testShop,
                0.0, 0.0, "Street", "2", "City", "12345", "State", "Country", 100
        );
        Warehouse warehouse3 = new Warehouse(
                "warehouse-3", "Warehouse 3", testShop,
                0.0, 0.0, "Street", "3", "City", "12345", "State", "Country", 100
        );
        List<Warehouse> warehouses = List.of(warehouse1, warehouse2, warehouse3);

        when(shopRepository.findById(shopId)).thenReturn(Optional.of(testShop));
        when(warehouseRepository.findByShop(testShop)).thenReturn(warehouses);
        when(shipmentRepository.findAllByWarehouseIn(warehouses))
                .thenReturn(List.of());

        // WHEN
        shipmentService.getAllShipmentsByShopId(shopId);

        // THEN
        verify(shopRepository).findById(shopId);
        verify(warehouseRepository).findByShop(testShop);
        verify(shipmentRepository).findAllByWarehouseIn(warehouses);
    }

    private void stubWarehouseMapper() {
        when(warehouseMapper.toWarehouseResponseDTO(any(Warehouse.class)))
                .thenAnswer(invocation -> mapWarehouse(invocation.getArgument(0)));
    }

    private WarehouseResponseDTO mapWarehouse(Warehouse warehouse) {
        return new WarehouseResponseDTO(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getShop(),
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
}

