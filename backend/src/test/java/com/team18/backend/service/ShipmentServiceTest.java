package com.team18.backend.service;

import com.team18.backend.dto.ShipmentCreateDTO;
import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.dto.ShipmentStatusUpdateDTO;
import com.team18.backend.dto.ShipmentUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentStatus;
import com.team18.backend.repository.ShipmentRepository;
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

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment testShipment;

    @BeforeEach
    void setUp() {
        testShipment = new Shipment(
                "shipment-123",
                "warehouse-1",
                LocalDate.of(2025, 12, 1),
                ShipmentStatus.ORDERED
        );
    }

    @Test
    void getAllShipments_shouldReturnAllShipments_whenRepositoryHasShipments() {
        // GIVEN
        Shipment shipment1 = new Shipment("warehouse-1", LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment shipment2 = new Shipment("warehouse-2", LocalDate.of(2025, 12, 5), ShipmentStatus.IN_DELIVERY);
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
        String shipmentId = "shipment-123";
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(testShipment));

        // WHEN
        ShipmentResponseDTO actualShipment = shipmentService.getShipmentById(shipmentId);

        // THEN
        assertThat(actualShipment).isNotNull();
        assertThat(actualShipment.id()).isEqualTo(shipmentId);
        assertThat(actualShipment.warehouseId()).isEqualTo("warehouse-1");
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
        String warehouseId = "warehouse-1";
        List<Shipment> expectedShipments = List.of(testShipment);
        when(shipmentRepository.findByWarehouseId(warehouseId)).thenReturn(expectedShipments);

        // WHEN
        List<ShipmentResponseDTO> actualShipments = shipmentService.getShipmentsByWarehouseId(warehouseId);

        // THEN
        assertThat(actualShipments).hasSize(1);
        assertThat(actualShipments.get(0).warehouseId()).isEqualTo(warehouseId);
        verify(shipmentRepository).findByWarehouseId(warehouseId);
    }

    @Test
    void createShipment_shouldSaveAndReturnShipment_whenValidDataProvided() {
        // GIVEN
        ShipmentCreateDTO dto = new ShipmentCreateDTO(
                "warehouse-1",
                LocalDate.of(2025, 12, 1),
                ShipmentStatus.ORDERED
        );
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(testShipment);

        // WHEN
        ShipmentResponseDTO actualShipment = shipmentService.createShipment(dto);

        // THEN
        assertThat(actualShipment).isNotNull();
        assertThat(actualShipment.warehouseId()).isEqualTo("warehouse-1");
        assertThat(actualShipment.status()).isEqualTo(ShipmentStatus.ORDERED);
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void updateShipment_shouldUpdateAndReturnShipment_whenShipmentExists() {
        // GIVEN
        String shipmentId = "shipment-123";
        ShipmentUpdateDTO dto = new ShipmentUpdateDTO(
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
}

