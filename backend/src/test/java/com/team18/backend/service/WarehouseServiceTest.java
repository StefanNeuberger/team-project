package com.team18.backend.service;

import com.team18.backend.dto.warehouse.WarehouseCreateDTO;
import com.team18.backend.dto.warehouse.WarehouseMapper;
import com.team18.backend.dto.warehouse.WarehouseResponseDTO;
import com.team18.backend.dto.warehouse.WarehouseUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.InventoryRepository;
import com.team18.backend.repository.ShopRepository;
import com.team18.backend.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WarehouseServiceTest {

    private static final WarehouseRepository warehouseRepository = Mockito.mock( WarehouseRepository.class );
    private static final ShopRepository shopRepository = Mockito.mock( ShopRepository.class );
    private static final InventoryRepository inventoryRepository = Mockito.mock( InventoryRepository.class );
    private static WarehouseMapper mapper = new WarehouseMapper();
    private static final WarehouseService warehouseService = new WarehouseService( warehouseRepository, shopRepository, inventoryRepository, mapper );

    private static final String fixedTestId = "test-id";
    private static Shop shop;
    private static Warehouse warehouse;
    private static Warehouse warehouseWithId;

    @BeforeAll
    static void setUp() {

        shop = new Shop(
                fixedTestId,
                "Test shop"
        );

        warehouse = new Warehouse(
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
        );
        warehouseWithId = new Warehouse(
                fixedTestId,
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
        );
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset( warehouseRepository, shopRepository, inventoryRepository );
    }

    @Test
    void getAllWarehouses_ShouldReturnList_WhenCalled() {
        //GIVEN
        List<Warehouse> warehouses = List.of( warehouse );
        List<WarehouseResponseDTO> warehouseResponseDTOS = mapper.toWarehouseResponseDTOList( warehouses );

        Mockito.when( warehouseRepository.save( warehouse ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findAll() ).thenReturn( warehouses );

        //WHEN
        warehouseRepository.save( warehouse );

        List<WarehouseResponseDTO> actual = warehouseService.getAllWarehouses();

        //THEN
        assertThat( actual )
                .isNotNull()
                .isNotEmpty()
                .containsAll( warehouseResponseDTOS );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findAll();
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).save( warehouse );
    }

    @Test
    void getAllWarehouses_ShouldReturnEmptyList_WhenCalled() {
        //GIVEN
        List<Warehouse> warehouses = new ArrayList<>();
        Mockito.when( warehouseRepository.findAll() ).thenReturn( warehouses );


        //WHEN
        List<WarehouseResponseDTO> actual = warehouseService.getAllWarehouses();

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEmpty();

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findAll();
    }

    @Test
    void getWarehouseById_ShouldReturnWarehouse_WhenCalled() {
        //GIVEN
        WarehouseResponseDTO warehouseResponseDTO = mapper.toWarehouseResponseDTO( warehouseWithId );

        Mockito.when( warehouseRepository.save( warehouseWithId ) ).thenReturn( warehouseWithId );
        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouseWithId ) );


        //WHEN
        warehouseRepository.save( warehouseWithId );

        WarehouseResponseDTO actual = assertDoesNotThrow( () -> {
            return warehouseService.getWarehouseById( fixedTestId );
        } );

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEqualTo( warehouseResponseDTO );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).save( warehouseWithId );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void getWarehouseById_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake";

        Mockito.when( warehouseRepository.save( warehouseWithId ) ).thenReturn( warehouseWithId );
        Mockito.when( warehouseRepository.findById( fakeId ) ).thenReturn( Optional.empty() );


        //WHEN
        warehouseRepository.save( warehouseWithId );

        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> {
            warehouseService.getWarehouseById( fakeId );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex )
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).save( warehouseWithId );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }

    @Test
    void createWarehouse_ShouldReturnWarehouse_WhenCalled() {
        //GIVEN
        WarehouseCreateDTO warehouseCreateDTO = new WarehouseCreateDTO(
                warehouse.getName(),
                warehouse.getShop().getId(),
                warehouse.getLat(),
                warehouse.getLng(),
                warehouse.getStreet(),
                warehouse.getNumber(),
                warehouse.getCity(),
                warehouse.getPostalCode(),
                warehouse.getState(),
                warehouse.getCountry(),
                warehouse.getMaxCapacity()
        );

        Warehouse newWarehouse = mapper.toWarehouse( warehouseCreateDTO, shop );

        WarehouseResponseDTO warehouseResponseDTO = mapper.toWarehouseResponseDTO( newWarehouse );
        Mockito.when( shopRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shop ) );
        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( newWarehouse );

        //WHEN
        WarehouseResponseDTO actual = warehouseService.createWarehouse( warehouseCreateDTO );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "name" )
                .isEqualTo( warehouseResponseDTO.name() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( shopRepository, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void updateWarehouse_ShouldReturnWarehouse_WhenCalled() {
        //GIVEN
        WarehouseUpdateDTO warehouseUpdateDTO = new WarehouseUpdateDTO(
                "Updated",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                21356
        );

        Warehouse updatedWarehouse = mapper.toWarehouse( warehouse, warehouseUpdateDTO, null );
        WarehouseResponseDTO updatedWarehouseResponse = mapper.toWarehouseResponseDTO( updatedWarehouse );

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );
        Mockito.when( warehouseRepository.save( Mockito.any( Warehouse.class ) ) ).thenReturn( updatedWarehouse );

        warehouseRepository.insert( warehouse );
        warehouseRepository.findById( fixedTestId ).orElseThrow();

        //WHEN
        WarehouseResponseDTO actual = assertDoesNotThrow( () -> warehouseService.updateWarehouse( fixedTestId, warehouseUpdateDTO ) );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "name" )
                .isEqualTo( updatedWarehouseResponse.name() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 2 ) ).findById( fixedTestId );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).save( Mockito.any( Warehouse.class ) );
    }

    @Test
    void updateWarehouse_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake";
        WarehouseUpdateDTO warehouseUpdateDTO = new WarehouseUpdateDTO(
                "Updated",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                21356
        );

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        warehouseRepository.insert( warehouse );

        //WHEN
        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> {
            warehouseService.updateWarehouse( fakeId, warehouseUpdateDTO );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex )
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }

    @Test
    void deleteWarehouse_ShouldReturnTrue_WhenCalled() {
        //GIVEN

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );

        warehouseRepository.insert( warehouse );

        //WHEN
        Boolean isDelete = assertDoesNotThrow( () -> warehouseService.deleteWarehouse( fixedTestId ) );

        //THEN
        assertThat( isDelete ).isTrue();

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).delete( Mockito.any( Warehouse.class ) );
    }

    @Test
    void deleteWarehouse_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake";


        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        warehouseRepository.insert( warehouse );


        //WHEN
        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> {
            warehouseService.deleteWarehouse( fakeId );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex )
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }
}