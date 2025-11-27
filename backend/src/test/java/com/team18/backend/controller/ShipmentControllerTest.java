package com.team18.backend.controller;

import com.team18.backend.dto.ShipmentResponseDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.ShipmentStatus;
import com.team18.backend.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShipmentService shipmentService;

    @Test
    void getAllShipments_shouldReturnListOfShipments_whenShipmentsExist() throws Exception {
        // GIVEN
        ShipmentResponseDTO shipment1 = new ShipmentResponseDTO(
                "shipment-1", "warehouse-1", LocalDate.of(2025, 12, 1), 
                ShipmentStatus.ORDERED, Instant.now(), Instant.now()
        );
        ShipmentResponseDTO shipment2 = new ShipmentResponseDTO(
                "shipment-2", "warehouse-2", LocalDate.of(2025, 12, 5), 
                ShipmentStatus.IN_DELIVERY, Instant.now(), Instant.now()
        );
        List<ShipmentResponseDTO> shipments = List.of(shipment1, shipment2);
        when(shipmentService.getAllShipments()).thenReturn(shipments);

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("shipment-1"))
                .andExpect(jsonPath("$[0].warehouseId").value("warehouse-1"))
                .andExpect(jsonPath("$[1].id").value("shipment-2"));

        verify(shipmentService).getAllShipments();
    }

    @Test
    void getAllShipments_shouldReturnEmptyList_whenNoShipmentsExist() throws Exception {
        // GIVEN
        when(shipmentService.getAllShipments()).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(shipmentService).getAllShipments();
    }

    @Test
    void getShipmentById_shouldReturnShipment_whenShipmentExists() throws Exception {
        // GIVEN
        String shipmentId = "shipment-123";
        ShipmentResponseDTO shipment = new ShipmentResponseDTO(
                shipmentId, "warehouse-1", LocalDate.of(2025, 12, 1), 
                ShipmentStatus.ORDERED, Instant.now(), Instant.now()
        );
        when(shipmentService.getShipmentById(eq(shipmentId))).thenReturn(shipment);

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments/{id}", shipmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shipmentId))
                .andExpect(jsonPath("$.warehouseId").value("warehouse-1"))
                .andExpect(jsonPath("$.status").value("ORDERED"));

        verify(shipmentService).getShipmentById(shipmentId);
    }

    @Test
    void getShipmentById_shouldReturn404_whenShipmentDoesNotExist() throws Exception {
        // GIVEN
        String shipmentId = "non-existent-id";
        when(shipmentService.getShipmentById(eq(shipmentId)))
                .thenThrow(new ResourceNotFoundException("Shipment not found with id: " + shipmentId));

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments/{id}", shipmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shipment not found with id: " + shipmentId))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(shipmentService).getShipmentById(shipmentId);
    }

    @Test
    void getShipmentsByWarehouseId_shouldReturnShipments_whenWarehouseHasShipments() throws Exception {
        // GIVEN
        String warehouseId = "warehouse-1";
        ShipmentResponseDTO shipment = new ShipmentResponseDTO(
                "shipment-1", warehouseId, LocalDate.of(2025, 12, 1), 
                ShipmentStatus.ORDERED, Instant.now(), Instant.now()
        );
        when(shipmentService.getShipmentsByWarehouseId(warehouseId)).thenReturn(List.of(shipment));

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments/warehouse/{warehouseId}", warehouseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].warehouseId").value(warehouseId));

        verify(shipmentService).getShipmentsByWarehouseId(warehouseId);
    }

    @Test
    void createShipment_shouldReturnCreatedShipment_whenValidRequestProvided() throws Exception {
        // GIVEN
        ShipmentResponseDTO createdShipment = new ShipmentResponseDTO(
                "shipment-456", "warehouse-1", LocalDate.of(2025, 12, 1), 
                ShipmentStatus.ORDERED, Instant.now(), Instant.now()
        );
        when(shipmentService.createShipment(any())).thenReturn(createdShipment);

        String requestBody = """
                {
                    "warehouseId": "warehouse-1",
                    "expectedArrivalDate": "2025-12-01",
                    "status": "ORDERED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(post("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("shipment-456"))
                .andExpect(jsonPath("$.warehouseId").value("warehouse-1"))
                .andExpect(jsonPath("$.status").value("ORDERED"));

        verify(shipmentService).createShipment(any());
    }

    @Test
    void createShipment_shouldReturn400_whenWarehouseIdIsBlank() throws Exception {
        // GIVEN
        String requestBody = """
                {
                    "warehouseId": "",
                    "expectedArrivalDate": "2025-12-01",
                    "status": "ORDERED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(post("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("warehouseId"))
                .andExpect(jsonPath("$.fieldErrors[0].message").exists());
    }

    @Test
    void createShipment_shouldReturn400_whenStatusIsMissing() throws Exception {
        // GIVEN
        String requestBody = """
                {
                    "warehouseId": "warehouse-1",
                    "expectedArrivalDate": "2025-12-01"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(post("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("status"))
                .andExpect(jsonPath("$.fieldErrors[0].message").exists());
    }

    @Test
    void updateShipment_shouldReturnUpdatedShipment_whenValidRequestProvided() throws Exception {
        // GIVEN
        String shipmentId = "shipment-123";
        ShipmentResponseDTO updatedShipment = new ShipmentResponseDTO(
                shipmentId, "warehouse-1", LocalDate.of(2025, 12, 15), 
                ShipmentStatus.IN_DELIVERY, Instant.now(), Instant.now()
        );
        when(shipmentService.updateShipment(eq(shipmentId), any())).thenReturn(updatedShipment);

        String requestBody = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(patch("/api/shipments/{id}", shipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shipmentId))
                .andExpect(jsonPath("$.status").value("IN_DELIVERY"));

        verify(shipmentService).updateShipment(eq(shipmentId), any());
    }

    @Test
    void updateShipmentStatus_shouldReturnUpdatedShipment_whenValidRequestProvided() throws Exception {
        // GIVEN
        String shipmentId = "shipment-123";
        ShipmentResponseDTO updatedShipment = new ShipmentResponseDTO(
                shipmentId, "warehouse-1", LocalDate.of(2025, 12, 1), 
                ShipmentStatus.IN_DELIVERY, Instant.now(), Instant.now()
        );
        when(shipmentService.updateShipmentStatus(eq(shipmentId), any())).thenReturn(updatedShipment);

        String requestBody = """
                {
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(patch("/api/shipments/{id}/status", shipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shipmentId))
                .andExpect(jsonPath("$.status").value("IN_DELIVERY"));

        verify(shipmentService).updateShipmentStatus(eq(shipmentId), any());
    }

    @Test
    void updateShipmentStatus_shouldReturn400_whenStatusIsMissing() throws Exception {
        // GIVEN
        String shipmentId = "shipment-123";
        String requestBody = """
                {
                }
                """;

        // WHEN & THEN
        mockMvc.perform(patch("/api/shipments/{id}/status", shipmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("status"))
                .andExpect(jsonPath("$.fieldErrors[0].message").exists());
    }

    @Test
    void deleteShipment_shouldReturn204_whenShipmentExists() throws Exception {
        // GIVEN
        String shipmentId = "shipment-123";

        // WHEN & THEN
        mockMvc.perform(delete("/api/shipments/{id}", shipmentId))
                .andExpect(status().isNoContent());

        verify(shipmentService).deleteShipment(shipmentId);
    }

    @Test
    void deleteShipment_shouldReturn404_whenShipmentDoesNotExist() throws Exception {
        // GIVEN
        String shipmentId = "non-existent-id";
        doThrow(new ResourceNotFoundException("Shipment not found with id: " + shipmentId))
                .when(shipmentService).deleteShipment(shipmentId);

        // WHEN & THEN
        mockMvc.perform(delete("/api/shipments/{id}", shipmentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shipment not found with id: " + shipmentId))
                .andExpect(jsonPath("$.status").value(404));

        verify(shipmentService).deleteShipment(shipmentId);
    }
}

