package com.team18.backend.service;

import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shop;
import com.team18.backend.repository.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    private ShopRepository shopRepository;

    @InjectMocks
    private ShopService shopService;

    private Shop testShop;

    @BeforeEach
    void setUp() {
        testShop = new Shop( "shop-123", "Test Shop" );
    }

    @Test
    void getAllShops_shouldReturnAllShops_whenRepositoryHasShops() {
        // GIVEN
        Shop shop1 = new Shop( "shop-1", "Shop One" );
        Shop shop2 = new Shop( "shop-2", "Shop Two" );
        List<Shop> expectedShops = List.of( shop1, shop2 );
        when( shopRepository.findAll() ).thenReturn( expectedShops );

        // WHEN
        List<Shop> actualShops = shopService.getAllShops();

        // THEN
        assertThat( actualShops ).hasSize( 2 );
        assertThat( actualShops ).containsExactly( shop1, shop2 );
        verify( shopRepository ).findAll();
    }

    @Test
    void getAllShops_shouldReturnEmptyList_whenRepositoryHasNoShops() {
        // GIVEN
        when( shopRepository.findAll() ).thenReturn( List.of() );

        // WHEN
        List<Shop> actualShops = shopService.getAllShops();

        // THEN
        assertThat( actualShops ).isEmpty();
        verify( shopRepository ).findAll();
    }

    @Test
    void getShopById_shouldReturnShop_whenShopExists() {
        // GIVEN
        String shopId = "shop-123";
        when( shopRepository.findById( shopId ) ).thenReturn( Optional.of( testShop ) );

        // WHEN
        Shop actualShop = shopService.getShopById( shopId );

        // THEN
        assertThat( actualShop ).isNotNull();
        assertThat( actualShop.getId() ).isEqualTo( shopId );
        assertThat( actualShop.getName() ).isEqualTo( "Test Shop" );
        verify( shopRepository ).findById( shopId );
    }

    @Test
    void getShopById_shouldThrowResourceNotFoundException_whenShopDoesNotExist() {
        // GIVEN
        String shopId = "non-existent-id";
        when( shopRepository.findById( shopId ) ).thenReturn( Optional.empty() );

        // WHEN & THEN
        assertThatThrownBy( () -> shopService.getShopById( shopId ) )
                .isInstanceOf( ResourceNotFoundException.class )
                .hasMessage( "Shop not found with id: " + shopId );
        verify( shopRepository ).findById( shopId );
    }

    @Test
    void createShop_shouldSaveAndReturnShop_whenValidNameProvided() {
        // GIVEN
        String shopName = "New Shop";
        Shop savedShop = new Shop( "shop-456", shopName );
        when( shopRepository.save( any( Shop.class ) ) ).thenReturn( savedShop );

        // WHEN
        Shop actualShop = shopService.createShop( shopName );

        // THEN
        assertThat( actualShop ).isNotNull();
        assertThat( actualShop.getId() ).isEqualTo( "shop-456" );
        assertThat( actualShop.getName() ).isEqualTo( shopName );
        verify( shopRepository ).save( any( Shop.class ) );
    }
}

