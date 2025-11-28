package com.team18.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.inventory.InventoryResponseDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.model.Item;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.ItemRepo;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.repository.WarehouseRepository;
import com.team18.backend.service.InventoryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest("spring.shell.interactive.enabled=false")
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private InventoryService inventoryService;

    private static ObjectMapper mapper = new ObjectMapper();
    private static InventoryCreateDTO newInventory;

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

        newInventory = new InventoryCreateDTO(
                warehouse.getId(),
                item.getId(),
                231312
        );
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenCalled() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/inventory" )
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
                                .content().json( """
                                          []
                                        """ )
                );
    }

    @Test
    void getAll_ShouldReturnSingleInList_WhenCalled() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/inventory" )
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
                                .jsonPath( "$[0].warehouse.id" ).value( inventoryResponseDTO.warehouse().id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].item.id" ).value( inventoryResponseDTO.item().getId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$[0].quantity" ).value( inventoryResponseDTO.quantity() )
                );
    }

    @Test
    void get_ShouldReturnSingleInventory_WhenCalled() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/inventory/" + inventoryResponseDTO.id() )
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
                                .jsonPath( "$.warehouse.id" ).value( inventoryResponseDTO.warehouse().id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( inventoryResponseDTO.item().getId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.quantity" ).value( inventoryResponseDTO.quantity() )
                );
    }

    @Test
    void get_ShouldReturnError_WhenCalled() throws Exception {
        inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/inventory/fakeid" )
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
    void post_ShouldReturnCreatedItem_WhenCalled() throws Exception {
        String jsonContent = mapper.writeValueAsString( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/inventory" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isCreated()
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
                                .jsonPath( "$.warehouse.id" ).value( newInventory.warehouseId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( newInventory.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.quantity" ).value( newInventory.quantity() )
                );
    }

    @Test
    void post_ShouldFail_WhenCalled() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/inventory" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( """
                                          {
                                            "warehouseId": null,
                                            "itemId": null,
                                            "quantity": "0"
                                          }
                                        """ )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isBadRequest()
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void update_ShouldFail_WhenCalled() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/inventory/" + inventoryResponseDTO.id() )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( """
                                          {
                                            "warehouseId": null,
                                            "itemId": null,
                                            "quantity": "0"
                                          }
                                        """ )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isBadRequest()
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }

    @Test
    void update_ShouldReturnUpdatedInventory_WhenCalled() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO(
                inventoryResponseDTO.warehouse().id(),
                inventoryResponseDTO.item().getId(),
                1232138
        );

        String jsonContent = mapper.writeValueAsString( inventoryUpdateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/inventory/" + inventoryResponseDTO.id() )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                ).andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.id" ).value( inventoryResponseDTO.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.warehouse.id" ).value( newInventory.warehouseId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( newInventory.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.quantity" ).value( inventoryUpdateDTO.quantity() )
                );
    }

    @Test
    void update_ShouldReturnUpdatedInventory_WhenCalledWithNullValues() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO(
                null,
                null,
                null
        );

        String jsonContent = mapper.writeValueAsString( inventoryUpdateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/inventory/" + inventoryResponseDTO.id() )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( jsonContent )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isOk()
                ).andExpect(
                        MockMvcResultMatchers
                                .content()
                                .contentType( MediaType.APPLICATION_JSON )
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.id" ).value( inventoryResponseDTO.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.warehouse.id" ).value( newInventory.warehouseId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.item.id" ).value( newInventory.itemId() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.quantity" ).value( inventoryResponseDTO.quantity() )
                );
    }

    @Test
    void delete_ShouldReturnTrue_WhenCalled() throws Exception {
        InventoryResponseDTO inventoryResponseDTO = inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/inventory/" + inventoryResponseDTO.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isNoContent()
                );
    }

    @Test
    void delete_ShouldFail_WhenCalled() throws Exception {
        inventoryService.createInventory( newInventory );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/inventory/fakeid" )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isNotFound()
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.message" ).isNotEmpty()
                );
    }
}