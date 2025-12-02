package com.team18.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemCreateDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemUpdateDTO;
import com.team18.backend.model.*;
import com.team18.backend.repository.*;
import com.team18.backend.service.ShipmentLineItemService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.shell.interactive.enabled=false")
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShipmentLineItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentLineItemRepository shipmentLineItemRepository;

    @Autowired
    private ShipmentLineItemService shipmentLineItemService;

    private static ObjectMapper mapper;
    private static ShipmentLineItemCreateDTO shipmentLineItemCreateDTO;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        mapper.registerModule( new JavaTimeModule() );
        mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
    }

    @BeforeEach
    void setUp() {
        // Clean up all data to avoid unique constraint violations
        shopRepository.deleteAll();
        warehouseRepository.deleteAll();
        itemRepo.deleteAll();
        shipmentRepository.deleteAll();
        shipmentLineItemRepository.deleteAll();

        Shop shop = shopRepository.save(
                new Shop(
                        "Test Shop"
                )
        );

        Warehouse warehouse = warehouseRepository.save(
                new Warehouse(
                        "Warehouse EU East",
                        shop,
                        52.179262, 20.9359542,
                        "Muszkieterow",
                        "26-32",
                        "Warszawa",
                        "02-273",
                        "",
                        "Poland",
                        4328974
                )
        );

        Item item = itemRepo.save(
                new Item(
                        null,
                        "SKU-TEST",
                        "TEST"
                )
        );

        Shipment shipment = shipmentRepository.save(
                new Shipment(
                        warehouse,
                        LocalDate.now(),
                        ShipmentStatus.ORDERED
                )
        );

        shipmentLineItemCreateDTO = new ShipmentLineItemCreateDTO(
                shipment.getId(),
                item.getId(),
                10,
                10
        );
    }

    @Test
    void getAllShipmentLineItemsByShipmentId_ShouldThrow_WhenNoShipmentLineItemsFound() throws Exception {
        String fakeId = "fake-id";

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/shipmentlineitems/byShipmentId/" + fakeId )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void getAllShipmentLineItemsByShipmentId_ShouldReturnShipmentLineItemResponseDTOList_WhenCalled() throws Exception {
        ShipmentLineItemResponseDTO data = shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/shipmentlineitems/byShipmentId/" + data.shipment().getId() )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].id" ).value( data.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].item.id" ).value( data.item().getId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].shipment.id" ).value( data.shipment().getId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].expectedQuantity" ).value( data.expectedQuantity() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].receivedQuantity" ).value( data.receivedQuantity() )
                );
    }

    @Test
    void createShipmentLineItem_ShouldThrow_WhenShipmentNotFound() throws Exception {
        ShipmentLineItemCreateDTO given = new ShipmentLineItemCreateDTO(
                "wrong-id",
                shipmentLineItemCreateDTO.itemId(),
                shipmentLineItemCreateDTO.expectedQuantity(),
                shipmentLineItemCreateDTO.receivedQuantity()
        );

        String jsonContent = mapper.writeValueAsString( given );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/shipmentlineitems" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void createShipmentLineItem_ShouldThrow_WhenItemNotFound() throws Exception {
        ShipmentLineItemCreateDTO given = new ShipmentLineItemCreateDTO(
                shipmentLineItemCreateDTO.shipmentId(),
                "wrong-id",
                shipmentLineItemCreateDTO.expectedQuantity(),
                shipmentLineItemCreateDTO.receivedQuantity()
        );

        String jsonContent = mapper.writeValueAsString( given );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/shipmentlineitems" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void createShipmentLineItem_ShouldReturnShipmentLineItemResponseDTO_WhenCalled() throws Exception {
        String jsonContent = mapper.writeValueAsString( shipmentLineItemCreateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/shipmentlineitems" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isCreated()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.id" ).isNotEmpty()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( shipmentLineItemCreateDTO.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.shipment.id" ).value( shipmentLineItemCreateDTO.shipmentId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.expectedQuantity" ).value( shipmentLineItemCreateDTO.expectedQuantity() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.receivedQuantity" ).value( shipmentLineItemCreateDTO.receivedQuantity() )
                );
    }

    @Test
    void updateShipmentLineItem_ShouldThrow_WhenShipmentLineItemNotFound() throws Exception {
        this.shipmentLineItemService.createShipmentLineItem( shipmentLineItemCreateDTO );

        ShipmentLineItemUpdateDTO updateDTO = new ShipmentLineItemUpdateDTO(
                null,
                null,
                null,
                null
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/shipmentlineitems/wrong-id" )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( mapper.writeValueAsString( updateDTO ) )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void updateShipmentLineItem_ShouldThrow_WhenShipmenNotFound() throws Exception {
        ShipmentLineItemResponseDTO data = this.shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        ShipmentLineItemUpdateDTO updateDTO = new ShipmentLineItemUpdateDTO(
                "wrong-id",
                shipmentLineItemCreateDTO.itemId(),
                20,
                20
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/shipmentlineitems/" + data.id() )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( mapper.writeValueAsString( updateDTO ) )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void updateShipmentLineItem_ShouldThrow_WhenItemNotFound() throws Exception {
        ShipmentLineItemResponseDTO data = this.shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        ShipmentLineItemUpdateDTO updateDTO = new ShipmentLineItemUpdateDTO(
                shipmentLineItemCreateDTO.shipmentId(),
                "wrong-id",
                20,
                20
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/shipmentlineitems/" + data.id() )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( mapper.writeValueAsString( updateDTO ) )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void updateShipmentLineItem_ShouldReturnShipmentLineItemResponseDTO_WhenReferencesAreNull() throws Exception {
        ShipmentLineItemResponseDTO data = this.shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        ShipmentLineItemUpdateDTO updateDTO = new ShipmentLineItemUpdateDTO(
                null,
                null,
                20,
                20
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/shipmentlineitems/" + data.id() )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( mapper.writeValueAsString( updateDTO ) )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isOk()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.id" ).value( data.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( shipmentLineItemCreateDTO.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.shipment.id" ).value( shipmentLineItemCreateDTO.shipmentId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.expectedQuantity" ).value( updateDTO.expectedQuantity() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.receivedQuantity" ).value( updateDTO.receivedQuantity() )
                );
    }

    @Test
    void updateShipmentLineItem_ShouldReturnShipmentLineItemResponseDTO_WhenReferencesAreNotNull() throws Exception {
        ShipmentLineItemResponseDTO data = this.shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        ShipmentLineItemUpdateDTO updateDTO = new ShipmentLineItemUpdateDTO(
                shipmentLineItemCreateDTO.shipmentId(),
                shipmentLineItemCreateDTO.itemId(),
                200,
                200
        );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/shipmentlineitems/" + data.id() )
                                .accept( MediaType.APPLICATION_JSON_VALUE )
                                .contentType( MediaType.APPLICATION_JSON_VALUE )
                                .content( mapper.writeValueAsString( updateDTO ) )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isOk()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.id" ).value( data.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( updateDTO.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.shipment.id" ).value( updateDTO.shipmentId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.expectedQuantity" ).value( updateDTO.expectedQuantity() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.receivedQuantity" ).value( updateDTO.receivedQuantity() )
                );
    }

    @Test
    void deleteShipmentLineItem_ShouldThrow_WhenShipmentLineItemNotFound() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/shipmentlineitems/wrong-id" )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNotFound()
                )
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void deleteShipmentLineItem_ShouldReturnTrue_WhenCalled() throws Exception {
        ShipmentLineItemResponseDTO data = shipmentLineItemService
                .createShipmentLineItem( shipmentLineItemCreateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/shipmentlineitems/" + data.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status()
                                .isNoContent()
                );
    }


}