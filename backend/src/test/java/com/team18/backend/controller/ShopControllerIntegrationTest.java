package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.model.Shop;
import com.team18.backend.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestContainersConfiguration.class)
class ShopControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopRepository shopRepository;

    @BeforeEach
    void setUp() {
        shopRepository.deleteAll();
    }

    @Test
    void createShop_shouldReturn409_whenShopNameAlreadyExists() throws Exception {
        // GIVEN - A shop with name "Duplicate Shop" already exists
        Shop existingShop = new Shop( "Duplicate Shop" );
        shopRepository.save( existingShop );

        String duplicateRequest = """
                {
                    "name": "Duplicate Shop"
                }
                """;

        // WHEN - We try to create another shop with the same name
        // THEN - Should return 409 Conflict with appropriate error message
        mockMvc.perform( post( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( duplicateRequest ) )
                .andExpect( status().isConflict() )
                .andExpect( jsonPath( "$.status" ).value( 409 ) )
                .andExpect( jsonPath( "$.error" ).value( "Conflict" ) )
                .andExpect( jsonPath( "$.message" ).value( containsString( "name already exists" ) ) );
    }

    @Test
    void createShop_shouldReturn201_whenShopNameIsUnique() throws Exception {
        // GIVEN - No existing shop with this name
        String request = """
                {
                    "name": "Unique Shop"
                }
                """;

        // WHEN & THEN - Should successfully create the shop
        mockMvc.perform( post( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( request ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.name" ).value( "Unique Shop" ) )
                .andExpect( jsonPath( "$.id" ).exists() );
    }
}

