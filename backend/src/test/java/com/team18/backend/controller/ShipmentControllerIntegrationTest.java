package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.model.*;
import com.team18.backend.repository.*;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("spring.shell.interactive.enabled=false")
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
class ShipmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ShipmentLineItemRepository shipmentLineItemRepository;

    @Autowired
    private ItemRepo itemRepo;

    private Warehouse testWarehouse;

    @BeforeEach
    void setUp() {
        shipmentRepository.deleteAll();
        warehouseRepository.deleteAll();
        shopRepository.deleteAll();

        // Create a test shop and warehouse for shipments to reference
        Shop testShop = shopRepository.save( new Shop( "Test Shop" ) );
        testWarehouse = warehouseRepository.save( new Warehouse(
                "Test Warehouse",
                testShop,
                0.0, 0.0,
                "Street", "1", "City", "12345", "State", "Country",
                100
        ) );
    }

    @Test
    void createShipment_shouldReturn201_whenValidShipmentProvided() throws Exception {
        // GIVEN
        String request = String.format( """
                {
                    "warehouseId": "%s",
                    "expectedArrivalDate": "2025-12-01",
                    "status": "ORDERED"
                }
                """, testWarehouse.getId() );

        // WHEN & THEN
        mockMvc.perform( post( "/api/shipments" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( request ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.warehouse.id" ).value( testWarehouse.getId() ) )
                .andExpect( jsonPath( "$.status" ).value( "ORDERED" ) )
                .andExpect( jsonPath( "$.id" ).exists() );
    }

    @Test
    void updateShipment_shouldReturn200_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.ORDERED );
        Shipment saved = shipmentRepository.save( existingShipment );

        String updateRequest = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.status" ).value( "IN_DELIVERY" ) )
                .andExpect( jsonPath( "$.expectedArrivalDate" ).value( "2025-12-15" ) );
    }

    @Test
    void updateShipment_shouldReturn200AndCreateInventory_whenStatusIsCompleted() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.IN_DELIVERY );
        Shipment saved = shipmentRepository.save( existingShipment );
        Item item = itemRepo.save( new Item( "test-id", "test-sku", "test-title" ) );
        ShipmentLineItem existingLineItem = shipmentLineItemRepository.save( new ShipmentLineItem( saved, item, 10, 10 ) );

        String updateRequest = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "COMPLETED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( saved.getId() ) );

        List<Inventory> inventories = inventoryRepository.findAllByWarehouse_Id( saved.getWarehouse().getId() ).orElse( List.of() );

        assertThat( inventories ).isNotNull().isNotEmpty()
                .extracting( "item.id" )
                .contains( existingLineItem.getItem().getId() );

    }

    @Test
    void updateShipment_shouldReturn200AndUpdateInventory_whenStatusIsCompleted() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.IN_DELIVERY );
        Shipment saved = shipmentRepository.save( existingShipment );
        Item item = itemRepo.save( new Item( "test-id", "test-sku", "test-title" ) );
        ShipmentLineItem existingLineItem = shipmentLineItemRepository.save( new ShipmentLineItem( saved, item, 10, 10 ) );
        inventoryRepository.save( new Inventory( saved.getWarehouse(), existingLineItem.getItem(), 10 ) );

        String updateRequest = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "COMPLETED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( saved.getId() ) );

        List<Inventory> inventories = inventoryRepository.findAllByWarehouse_Id( saved.getWarehouse().getId() ).orElse( List.of() );

        assertThat( inventories ).isNotNull().isNotEmpty()
                .extracting( "item.id", "quantity" )
                .contains( Tuple.tuple( existingLineItem.getItem().getId(), 20 ) );

    }

    @Test
    void updateShipment_shouldReturn400_whenShipmentIsLocked() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.COMPLETED );
        Shipment saved = shipmentRepository.save( existingShipment );

        String updateRequest = """
                {
                    "expectedArrivalDate": "2025-12-15",
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath( "$.message" ).isNotEmpty() );
    }

    @Test
    void updateShipmentStatus_shouldReturn200_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.ORDERED );
        Shipment saved = shipmentRepository.save( existingShipment );

        String statusUpdateRequest = """
                {
                    "status": "PROCESSED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}/status", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( statusUpdateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.status" ).value( "PROCESSED" ) );
    }

    @Test
    void updateShipmentStatus_shouldReturn400_whenShipmentIsLocked() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.COMPLETED );
        Shipment saved = shipmentRepository.save( existingShipment );

        String updateRequest = """
                {
                    "status": "IN_DELIVERY"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}/status", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath( "$.message" ).isNotEmpty() );
    }

    @Test
    void updateShipmentStatus_shouldReturn200AndCreateInventory_whenStatusIsCompleted() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.IN_DELIVERY );
        Shipment saved = shipmentRepository.save( existingShipment );
        Item item = itemRepo.save( new Item( "test-id", "test-sku", "test-title" ) );
        ShipmentLineItem existingLineItem = shipmentLineItemRepository.save( new ShipmentLineItem( saved, item, 10, 10 ) );

        String updateRequest = """
                {
                    "status": "COMPLETED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}/status", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( saved.getId() ) );

        List<Inventory> inventories = inventoryRepository.findAllByWarehouse_Id( saved.getWarehouse().getId() ).orElse( List.of() );

        assertThat( inventories ).isNotNull().isNotEmpty()
                .extracting( "item.id" )
                .contains( existingLineItem.getItem().getId() );

    }

    @Test
    void updateShipmentStatus_shouldReturn200AndUpdateInventory_whenStatusIsCompleted() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.IN_DELIVERY );
        Shipment saved = shipmentRepository.save( existingShipment );
        Item item = itemRepo.save( new Item( "test-id", "test-sku", "test-title" ) );
        ShipmentLineItem existingLineItem = shipmentLineItemRepository.save( new ShipmentLineItem( saved, item, 10, 10 ) );
        inventoryRepository.save( new Inventory( saved.getWarehouse(), existingLineItem.getItem(), 10 ) );

        String updateRequest = """
                {
                    "status": "COMPLETED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( patch( "/api/shipments/{id}/status", saved.getId() )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( updateRequest ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( saved.getId() ) );

        List<Inventory> inventories = inventoryRepository.findAllByWarehouse_Id( saved.getWarehouse().getId() ).orElse( List.of() );

        assertThat( inventories ).isNotNull().isNotEmpty()
                .extracting( "item.id", "quantity" )
                .contains( Tuple.tuple( existingLineItem.getItem().getId(), 20 ) );

    }

    @Test
    void deleteShipment_shouldReturn204_whenShipmentExists() throws Exception {
        // GIVEN - Create a shipment first
        Shipment existingShipment = new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.ORDERED );
        Shipment saved = shipmentRepository.save( existingShipment );

        // WHEN & THEN
        mockMvc.perform( delete( "/api/shipments/{id}", saved.getId() ) )
                .andExpect( status().isNoContent() );
    }

    @Test
    void getShipmentsByWarehouseId_shouldReturnShipments_whenWarehouseHasShipments() throws Exception {
        // GIVEN - Create shipments for a warehouse
        String warehouseId = testWarehouse.getId();
        shipmentRepository.save( new Shipment( testWarehouse, LocalDate.of( 2025, 12, 1 ), ShipmentStatus.ORDERED ) );
        shipmentRepository.save( new Shipment( testWarehouse, LocalDate.of( 2025, 12, 5 ), ShipmentStatus.IN_DELIVERY ) );

        // WHEN & THEN
        mockMvc.perform( get( "/api/shipments/warehouse/{warehouseId}", warehouseId )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$" ).isArray() )
                .andExpect( jsonPath( "$[0].warehouse.id" ).value( warehouseId ) )
                .andExpect( jsonPath( "$[1].warehouse.id" ).value( warehouseId ) );
    }

    @Test
    void createShipment_shouldReturn404_whenWarehouseDoesNotExist() throws Exception {
        // GIVEN
        String request = """
                {
                    "warehouseId": "non-existent-warehouse",
                    "expectedArrivalDate": "2025-12-01",
                    "status": "ORDERED"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( post( "/api/shipments" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( request ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "Warehouse not found with id: non-existent-warehouse" ) )
                .andExpect( jsonPath( "$.status" ).value( 404 ) )
                .andExpect( jsonPath( "$.error" ).value( "Not Found" ) );
    }
}

