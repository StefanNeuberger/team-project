package com.team18.backend.service;

import com.team18.backend.dto.WarehouseCreateDTO;
import com.team18.backend.dto.WarehouseMapper;
import com.team18.backend.dto.WarehouseResponseDTO;
import com.team18.backend.dto.WarehouseUpdateDTO;
import com.team18.backend.exception.WarehouseNotFoundException;
import com.team18.backend.model.Warehouse;
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
    private static WarehouseMapper mapper;

    private static final String fixedTestId = "test-id";
    private static Warehouse warehouse;
    private static Warehouse warehouseWithId;

    @BeforeAll
    static void setUp() {
        warehouse = new Warehouse(
                "Warehouse EU East",
                52.179262, 20.9359542,
                "Muszkieterow",
                "26-32",
                "Warszawa",
                "02-273",
                "",
                "Poland",
                4328974
        );
        mapper = new WarehouseMapper();
        warehouseWithId = new Warehouse(
                fixedTestId,
                "Warehouse EU East",
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
        Mockito.reset( warehouseRepository );
    }

    @Test
    void getAllWarehouses_ShouldReturnList_WhenCalled() {
        //GIVEN
        List<Warehouse> warehouses = List.of( warehouse );
        List<WarehouseResponseDTO> warehouseResponseDTOS = mapper.toWarehouseResponseDTOList( warehouses );

        Mockito.when( warehouseRepository.save( warehouse ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findAll() ).thenReturn( warehouses );

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

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

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

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

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

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

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

        //WHEN
        warehouseRepository.save( warehouseWithId );

        WarehouseNotFoundException ex = assertThrows( WarehouseNotFoundException.class, () -> {
            warehouseService.getWarehouseById( fakeId );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex.getMessage() )
                .isEqualTo( new WarehouseNotFoundException( fakeId ).getMessage() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).save( warehouseWithId );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }

    @Test
    void createWarehouse_ShouldReturnWarehouse_WhenCalled() {
        //GIVEN
        WarehouseCreateDTO warehouseCreateDTO = new WarehouseCreateDTO(
                warehouse.getName(),
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

        Warehouse newWarehouse = mapper.toWarehouse( warehouseCreateDTO );

        WarehouseResponseDTO warehouseResponseDTO = mapper.toWarehouseResponseDTO( newWarehouse );

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( newWarehouse );

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

        //WHEN
        WarehouseResponseDTO actual = warehouseService.createWarehouse( warehouseCreateDTO );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "name" )
                .isEqualTo( warehouseResponseDTO.name() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
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
                21356
        );

        Warehouse updatedWarehouse = mapper.toWarehouse( warehouse, warehouseUpdateDTO );
        WarehouseResponseDTO updatedWarehouseResponse = mapper.toWarehouseResponseDTO( updatedWarehouse );

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );
        Mockito.when( warehouseRepository.save( Mockito.any( Warehouse.class ) ) ).thenReturn( updatedWarehouse );

        warehouseRepository.insert( warehouse );
        warehouseRepository.findById( fixedTestId ).orElseThrow();
        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

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
                21356
        );

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        warehouseRepository.insert( warehouse );
        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

        //WHEN
        WarehouseNotFoundException ex = assertThrows( WarehouseNotFoundException.class, () -> {
            warehouseService.updateWarehouse( fakeId, warehouseUpdateDTO );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex.getMessage() )
                .isEqualTo( new WarehouseNotFoundException( fakeId ).getMessage() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }

    @Test
    void deleteWarehouse_ShouldReturnTrue_WhenCalled() {
        //GIVEN

        Mockito.when( warehouseRepository.insert( Mockito.any( Warehouse.class ) ) ).thenReturn( warehouse );
        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );

        warehouseRepository.insert( warehouse );

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

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

        WarehouseService warehouseService = new WarehouseService( warehouseRepository, mapper );

        //WHEN
        WarehouseNotFoundException ex = assertThrows( WarehouseNotFoundException.class, () -> {
            warehouseService.deleteWarehouse( fakeId );
        } );

        //THEN
        assertThat( ex )
                .isNotNull();

        assertThat( ex.getMessage() )
                .isEqualTo( new WarehouseNotFoundException( fakeId ).getMessage() );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).insert( Mockito.any( Warehouse.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fakeId );
    }
}