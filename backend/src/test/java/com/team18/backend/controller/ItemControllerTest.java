package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.model.Item;
import com.team18.backend.repository.ItemRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepo itemRepo;

    @Test
    void findAllItems() throws Exception {
        // Given
        Item item1 = new Item( null, "SKU001", "Test Item 1" );
        Item item2 = new Item( null, "SKU002", "Test Item 2" );
        itemRepo.save( item1 );
        itemRepo.save( item2 );

        // When & Then
        mockMvc.perform( get( "/api/item" ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 2 ) )
                .andExpect( jsonPath( "$[0].name" ).value( "Test Item 1" ) )
                .andExpect( jsonPath( "$[1].name" ).value( "Test Item 2" ) )
                .andExpect( jsonPath( "$[0].id" ).isNotEmpty() )
                .andExpect( jsonPath( "$[1].id" ).isNotEmpty() );
    }

    @Test
    void findItemById() {
    }

    @Test
    void createItem() {
    }

    @Test
    void updateItemById() {
    }

    @Test
    void deleteItemById() {
    }
}