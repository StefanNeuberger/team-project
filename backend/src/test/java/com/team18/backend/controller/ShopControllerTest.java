package com.team18.backend.controller;

import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shop;
import com.team18.backend.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopController.class)
class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShopService shopService;

    @Test
    void getAllShops_shouldReturnListOfShops_whenShopsExist() throws Exception {
        // GIVEN
        Shop shop1 = new Shop( "shop-1", "Shop One" );
        Shop shop2 = new Shop( "shop-2", "Shop Two" );
        List<Shop> shops = List.of( shop1, shop2 );
        when( shopService.getAllShops() ).thenReturn( shops );

        // WHEN & THEN
        mockMvc.perform( get( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$" ).isArray() )
                .andExpect( jsonPath( "$[0].id" ).value( "shop-1" ) )
                .andExpect( jsonPath( "$[0].name" ).value( "Shop One" ) )
                .andExpect( jsonPath( "$[1].id" ).value( "shop-2" ) )
                .andExpect( jsonPath( "$[1].name" ).value( "Shop Two" ) );

        verify( shopService ).getAllShops();
    }

    @Test
    void getAllShops_shouldReturnEmptyList_whenNoShopsExist() throws Exception {
        // GIVEN
        when( shopService.getAllShops() ).thenReturn( List.of() );

        // WHEN & THEN
        mockMvc.perform( get( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$" ).isArray() )
                .andExpect( jsonPath( "$" ).isEmpty() );

        verify( shopService ).getAllShops();
    }

    @Test
    void getShopById_shouldReturnShop_whenShopExists() throws Exception {
        // GIVEN
        String shopId = "shop-123";
        Shop shop = new Shop( shopId, "Test Shop" );
        when( shopService.getShopById( eq( shopId ) ) ).thenReturn( shop );

        // WHEN & THEN
        mockMvc.perform( get( "/api/shops/{id}", shopId )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( shopId ) )
                .andExpect( jsonPath( "$.name" ).value( "Test Shop" ) );

        verify( shopService ).getShopById( shopId );
    }

    @Test
    void getShopById_shouldReturn404_whenShopDoesNotExist() throws Exception {
        // GIVEN
        String shopId = "non-existent-id";
        when( shopService.getShopById( eq( shopId ) ) )
                .thenThrow( new ResourceNotFoundException( "Shop not found with id: " + shopId ) );

        // WHEN & THEN
        mockMvc.perform( get( "/api/shops/{id}", shopId )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "Shop not found with id: " + shopId ) )
                .andExpect( jsonPath( "$.status" ).value( 404 ) )
                .andExpect( jsonPath( "$.error" ).value( "Not Found" ) );

        verify( shopService ).getShopById( shopId );
    }

    @Test
    void createShop_shouldReturnCreatedShop_whenValidRequestProvided() throws Exception {
        // GIVEN
        String shopName = "New Shop";
        Shop createdShop = new Shop( "shop-456", shopName );
        when( shopService.createShop( eq( shopName ) ) ).thenReturn( createdShop );

        String requestBody = """
                {
                    "name": "New Shop"
                }
                """;

        // WHEN & THEN
        mockMvc.perform( post( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( requestBody ) )
                .andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.id" ).value( "shop-456" ) )
                .andExpect( jsonPath( "$.name" ).value( shopName ) );

        verify( shopService ).createShop( shopName );
    }

    @Test
    void createShop_shouldReturn400_whenNameIsBlank() throws Exception {
        // GIVEN
        String requestBody = """
                {
                    "name": ""
                }
                """;

        // WHEN & THEN
        mockMvc.perform( post( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( requestBody ) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath( "$.message" ).value( "Validation failed" ) )
                .andExpect( jsonPath( "$.status" ).value( 400 ) )
                .andExpect( jsonPath( "$.fieldErrors[0].field" ).value( "name" ) )
                .andExpect( jsonPath( "$.fieldErrors[0].message" ).exists() );
    }

    @Test
    void createShop_shouldReturn400_whenNameIsMissing() throws Exception {
        // GIVEN
        String requestBody = """
                {
                }
                """;

        // WHEN & THEN
        mockMvc.perform( post( "/api/shops" )
                        .contentType( MediaType.APPLICATION_JSON )
                        .content( requestBody ) )
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath( "$.message" ).value( "Validation failed" ) )
                .andExpect( jsonPath( "$.status" ).value( 400 ) )
                .andExpect( jsonPath( "$.fieldErrors[0].field" ).value( "name" ) )
                .andExpect( jsonPath( "$.fieldErrors[0].message" ).exists() );
    }
}

