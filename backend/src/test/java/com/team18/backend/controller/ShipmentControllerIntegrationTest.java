package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.model.Shipment;
import com.team18.backend.model.ShipmentStatus;
import com.team18.backend.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
class ShipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @BeforeEach
    void setUp() {
        shipmentRepository.deleteAll();
    }

    @Test
    void createShipment_shouldReturn201_whenValidShipmentProvided() throws Exception {
        // GIVEN
        String request = """
                {
                    "warehouseId": "warehouse-1",
                    "expectedArrivalDate": "2025-12-01",
                    "status": "ORDERED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(post("/api/shipments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouseId").value("warehouse-1"))
                .andExpect(jsonPath("$.status").value("ORDERED"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateShipment_shouldReturn200_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment("warehouse-1", LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment saved = shipmentRepository.save(existingShipment);

        String updateRequest = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(patch("/api/shipments/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_DELIVERY"))
                .andExpect(jsonPath("$.expectedArrivalDate").value("2025-12-15"));
    }

    @Test
    void updateShipmentStatus_shouldReturn200_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment("warehouse-1", LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment saved = shipmentRepository.save(existingShipment);

        String statusUpdateRequest = """
                {
                    "status": "PROCESSED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform(patch("/api/shipments/{id}/status", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));
    }

    @Test
    void deleteShipment_shouldReturn204_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment("warehouse-1", LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED);
        Shipment saved = shipmentRepository.save(existingShipment);

        // WHEN & THEN
        mockMvc.perform(delete("/api/shipments/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getShipmentsByWarehouseId_shouldReturnShipments_whenWarehouseHasShipments() throws Exception {
        // GIVEN - Create shipments for a warehouse
        shipmentRepository.save(new Shipment("warehouse-1", LocalDate.of(2025, 12, 1), ShipmentStatus.ORDERED));
        shipmentRepository.save(new Shipment("warehouse-1", LocalDate.of(2025, 12, 5), ShipmentStatus.IN_DELIVERY));

        // WHEN & THEN
        mockMvc.perform(get("/api/shipments/warehouse/warehouse-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].warehouseId").value("warehouse-1"))
                .andExpect(jsonPath("$[1].warehouseId").value("warehouse-1"));
    }
}

