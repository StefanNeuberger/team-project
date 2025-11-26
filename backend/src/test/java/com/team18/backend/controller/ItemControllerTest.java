package com.team18.backend.controller;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.model.Item;
import com.team18.backend.repository.ItemRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@Import(TestContainersConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepo itemRepo;

    @Nested
    class DeleteItemTests {
        @Test
        @DisplayName("should delete item when found")
        void deleteItemById_found() throws Exception {
            Item item = new Item( null, "SKU001", "Test Item 1" );
            item = itemRepo.save( item );

            mockMvc.perform( delete( "/api/item/" + item.getId() ) )
                    .andExpect( status().isNoContent() );
        }

        @Test
        @DisplayName("should return 404 when not found")
        void deleteItemById_notFound() throws Exception {
            String nonExistingId = "666";

            mockMvc.perform( delete( "/api/item/" + nonExistingId )
                            .contentType( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isNotFound() )
                    .andExpect( jsonPath( "$.message" ).value( "Item not found: " + nonExistingId ) )
                    .andExpect( jsonPath( "$.status" ).value( 404 ) )
                    .andExpect( jsonPath( "$.error" ).value( "Not Found" ) );

        }

        @Test
        @DisplayName("should delete all items")
        void deleteAllItems() throws Exception {
            Item item1 = new Item( null, "SKU001", "Test Item 1" );
            Item item2 = new Item( null, "SKU002", "Test Item 2" );

            itemRepo.save( item1 );
            itemRepo.save( item2 );

            mockMvc.perform( delete( "/api/item" ) )
                    .andExpect( status().isNoContent() );
        }
    }

    @Nested
    class CreateItemTests {
        @Test
        @DisplayName("should return item when created")
        void createItem() throws Exception {
            String itemName = "Test Item 3";

            String requestBody = """
                    {
                        "name": "Test Item 3",
                        "sku": "SKU003"
                    }
                    """;


            mockMvc.perform( post( "/api/item" )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( requestBody ) )
                    .andExpect( status().isCreated() )
                    .andExpect( jsonPath( "$.id" ).isNotEmpty() )
                    .andExpect( jsonPath( "$.name" ).value( itemName ) );
        }

        @Test
        @DisplayName("should return 400 when bad request")
        void createItem_invalidRequest() throws Exception {

            // missing name field
            String requestBody = """
                    {
                        "sku": "SKU003"
                    }
                    """;


            mockMvc.perform( post( "/api/item" )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( requestBody ) )
                    .andExpect( status().isBadRequest() )
                    .andDo( print() )
                    .andExpect( jsonPath( "$.message" ).value( "Validation failed" ) )
                    .andExpect( jsonPath( "$.status" ).value( 400 ) );

        }

    }

    @Nested
    class UpdateItemTests {
        @Test
        @DisplayName("should return updated item when successful")
        void updateItemById() throws Exception {
            String updatedName = "Test Item 2";
            Item item = new Item( null, "SKU001", "Test Item 1" );
            item = itemRepo.save( item );

            String requestBody = """
                    {
                        "name": "Test Item 2",
                        "sku": "SKU001"
                    }
                    """;

            mockMvc.perform( put( "/api/item/" + item.getId() )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( requestBody ) )
                    .andExpect( status().isOk() )
                    .andExpect( jsonPath( "$.name" ).value( updatedName ) )
                    .andExpect( jsonPath( "$.id" ).value( item.getId() ) );
        }

        @Test
        @DisplayName("should return 404 when not found")
        void updateItemById_notFound() throws Exception {
            String nonExistingId = "666";


            String requestBody = """
                    {
                        "name": "Test Item 2",
                        "sku": "SKU001"
                    }
                    """;


            mockMvc.perform( put( "/api/item/" + nonExistingId )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( requestBody ) )
                    .andExpect( status().isNotFound() )
                    .andExpect( jsonPath( "$.message" ).value( "Item not found: " + nonExistingId ) )
                    .andExpect( jsonPath( "$.status" ).value( 404 ) )
                    .andExpect( jsonPath( "$.error" ).value( "Not Found" ) );

        }

        @Test
        @DisplayName("should return 400 when bad request")
        void updateItemById_invalidRequest() throws Exception {
            Item item = new Item( null, "SKU001", "Test Item 1" );
            item = itemRepo.save( item );

            // missing name field
            String requestBody = """
                    {
                        "sku": "SKU002"
                    }
                    """;


            mockMvc.perform( put( "/api/item/" + item.getId() )
                            .contentType( MediaType.APPLICATION_JSON )
                            .content( requestBody ) )
                    .andExpect( status().isBadRequest() )
                    .andDo( print() )
                    .andExpect( jsonPath( "$.message" ).value( "Validation failed" ) )
                    .andExpect( jsonPath( "$.status" ).value( 400 ) );

        }
    }

    @Nested
    class FindItemsTests {
        @Test
        @DisplayName("should return list of item when existing")
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
        @DisplayName("should return empty list when no items exist")
        void findAllItems_shouldReturnEmpty() throws Exception {

            mockMvc.perform( get( "/api/item" ) )
                    .andExpect( status().isOk() )
                    .andExpect( jsonPath( "$.length()" ).value( 0 ) )
                    .andExpect( jsonPath( "$" ).isArray() )
                    .andExpect( jsonPath( "$" ).isEmpty() );
        }

        @Test
        @DisplayName("should return item when found")
        void findItemById() throws Exception {
            Item item = new Item( null, "SKU001", "Test Item 1" );
            item = itemRepo.save( item );

            mockMvc.perform( get( "/api/item/" + item.getId() ) )
                    .andExpect( status().isOk() )
                    .andExpect( jsonPath( "$.name" ).value( "Test Item 1" ) );
        }

        @Test
        @DisplayName("should return 404 when not found")
        void findItemById_notFound() throws Exception {
            String nonExistingId = "666";

            mockMvc.perform( get( "/api/item/" + nonExistingId )
                            .contentType( MediaType.APPLICATION_JSON ) )
                    .andExpect( status().isNotFound() )
                    .andExpect( jsonPath( "$.message" ).value( "Item not found: " + nonExistingId ) )
                    .andExpect( jsonPath( "$.status" ).value( 404 ) )
                    .andExpect( jsonPath( "$.error" ).value( "Not Found" ) );

        }

    }
}