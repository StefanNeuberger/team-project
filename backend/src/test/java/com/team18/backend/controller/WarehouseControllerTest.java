package com.team18.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.dto.warehouse.WarehouseCreateDTO;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.dto.warehouse.WarehouseUpdateDTO;
import com.team18.backend.model.Shop;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.service.WarehouseService;
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

import java.util.List;

@SpringBootTest("spring.shell.interactive.enabled=false")
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WarehouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private ShopRepository shopRepository;

    private static ObjectMapper mapper;
    private static Shop shop;
    private static WarehouseCreateDTO newWarehouse;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        mapper.registerModule( new JavaTimeModule() );
        mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
    }

    @BeforeEach
    void setUp() {
        shop = shopRepository.save(
                new Shop(
                        "Test Shop"
                )
        );

        newWarehouse = new WarehouseCreateDTO(
                "Warehouse EU East",
                shop.getId(),
                52.179262, 20.9359542,
                "Muszkieterow",
                "26-32",
                "Warszawa",
                "02-273",
                "",
                "Poland",
                4328974
        );
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenCalled() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/warehouses" )
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
        WarehouseResponseDTO warehouse = warehouseService.createWarehouse( newWarehouse );

        String jsonContent = mapper.writeValueAsString( List.of( warehouse ) );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/warehouses" )
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
                                .content().json( jsonContent )
                );
    }

    @Test
    void get_ShouldReturnSingleWareHouse_WhenCalled() throws Exception {
        WarehouseResponseDTO warehouse = warehouseService.createWarehouse( newWarehouse );

        String jsonContent = mapper.writeValueAsString( warehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/warehouses/" + warehouse.id() )
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
                                .content().json( jsonContent )
                );
    }

    @Test
    void get_ShouldReturnError_WhenCalled() throws Exception {
        warehouseService.createWarehouse( newWarehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get( "/api/warehouses/fakeid" )
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
        String jsonContent = mapper.writeValueAsString( newWarehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/warehouses" )
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
                );
    }

    @Test
    void post_ShouldFail_WhenCalled() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post( "/api/warehouses" )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( """
                                          {
                                            "name": "",
                                            "maxCapacity": "0"
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
        WarehouseResponseDTO warehouse = warehouseService.createWarehouse( newWarehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/warehouses/" + warehouse.id() )
                                .contentType( MediaType.APPLICATION_JSON )
                                .content( """
                                          {
                                            "name": "",
                                            "maxCapacity": "0"
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
    void update_ShouldReturnUpdatedWarehouse_WhenCalled() throws Exception {
        WarehouseResponseDTO warehouse = warehouseService.createWarehouse( newWarehouse );

        WarehouseUpdateDTO warehouseUpdateDTO = new WarehouseUpdateDTO(
                "Update",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        String jsonContent = mapper.writeValueAsString( warehouseUpdateDTO );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .patch( "/api/warehouses/" + warehouse.id() )
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
                                .jsonPath( "$.id" ).value( warehouse.id() )
                ).andExpect(
                        MockMvcResultMatchers
                                .jsonPath( "$.name" ).value( warehouseUpdateDTO.name() )
                );
    }

    @Test
    void delete_ShouldReturnTrue_WhenCalled() throws Exception {
        WarehouseResponseDTO warehouse = warehouseService.createWarehouse( newWarehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/warehouses/" + warehouse.id() )
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isNoContent()
                );
    }

    @Test
    void delete_ShouldFail_WhenCalled() throws Exception {
        warehouseService.createWarehouse( newWarehouse );

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete( "/api/warehouses/fakeid" )
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