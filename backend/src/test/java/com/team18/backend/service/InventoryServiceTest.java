package com.team18.backend.service;

import com.team18.backend.dto.inventory.InventoryCreateDTO;
import com.team18.backend.dto.inventory.InventoryMapper;
import com.team18.backend.dto.inventory.InventoryResponseDTO;
import com.team18.backend.dto.inventory.InventoryUpdateDTO;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Inventory;
import com.team18.backend.model.Item;
import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import com.team18.backend.repository.InventoryRepository;
import com.team18.backend.repository.ItemRepo;
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

class InventoryServiceTest {

    private static final WarehouseRepository warehouseRepository = Mockito.mock( WarehouseRepository.class );
    private static final ItemRepo itemRepo = Mockito.mock( ItemRepo.class );
    private static final InventoryRepository inventoryRepository = Mockito.mock( InventoryRepository.class );
    private static final InventoryMapper mapper = new InventoryMapper();
    private static final InventoryService inventoryService = new InventoryService(
            inventoryRepository,
            mapper,
            warehouseRepository,
            itemRepo
    );

    private static final String fixedTestId = "test-id";
    private static Warehouse warehouse;
    private static Item item;
    private static Inventory inventory;
    private static Inventory inventoryWithId;

    @BeforeAll
    static void setUp() {

        Shop shop = new Shop(
                fixedTestId,
                "Test shop"
        );

        warehouse = new Warehouse(
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

        item = new Item(
                fixedTestId,
                "SKU-Test",
                "Name-Test"
        );

        inventory = new Inventory(
                warehouse,
                item,
                10
        );

        inventoryWithId = new Inventory(
                fixedTestId,
                warehouse,
                item,
                10
        );

    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset( inventoryRepository, warehouseRepository, itemRepo );
    }

    @Test
    void getAllInventory_ShouldReturnList_WhenCalled() {
        //GIVEN
        List<Inventory> inventoryList = List.of( inventory );
        List<InventoryResponseDTO> inventoryResponseDTOList = mapper.toInventoryResponseDTOList( inventoryList );

        Mockito.when( inventoryRepository.save( inventory ) ).thenReturn( inventory );
        Mockito.when( inventoryRepository.findAll() ).thenReturn( inventoryList );

        //WHEN
        inventoryRepository.save( inventory );

        List<InventoryResponseDTO> actual = inventoryService.getAllInventory();

        //THEN
        assertThat( actual )
                .isNotNull()
                .isNotEmpty()
                .containsAll( inventoryResponseDTOList );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAll();
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventory );
    }

    @Test
    void getAllInventory_ShouldReturnEmptyList_WhenCalled() {
        //GIVEN
        List<Inventory> inventoryList = new ArrayList<>();
        Mockito.when( inventoryRepository.findAll() ).thenReturn( inventoryList );

        //WHEN
        List<InventoryResponseDTO> actual = inventoryService.getAllInventory();

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEmpty();

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAll();
    }

    @Test
    void getInventoryById_ShouldReturnInventory_WhenCalled() {
        //GIVEN
        InventoryResponseDTO inventoryResponseDTO = mapper.toInventoryResponseDTO( inventoryWithId );

        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findById( fixedTestId ) ).thenReturn( Optional.of( inventoryWithId ) );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        InventoryResponseDTO actual = assertDoesNotThrow( () -> inventoryService.getInventoryById( fixedTestId ) );

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEqualTo( inventoryResponseDTO );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void getInventoryById_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake-id";

        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> inventoryService.getInventoryById( fakeId ) );

        assertThat( ex )
                .isNotNull()
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findById( fakeId );
    }

    @Test
    void getInventoryByWarehouseId_ShouldReturnInventoryList_WhenCalled() {
        //GIVEN
        List<InventoryResponseDTO> inventoryResponseDTO = mapper.toInventoryResponseDTOList( List.of( inventoryWithId ) );


        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findAllByWarehouse_Id( fixedTestId ) ).thenReturn( Optional.of( List.of( inventoryWithId ) ) );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        List<InventoryResponseDTO> actual = assertDoesNotThrow( () -> inventoryService.getInventoryByWarehouseId( fixedTestId ) );

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEqualTo( inventoryResponseDTO );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAllByWarehouse_Id( fixedTestId );
    }

    @Test
    void getInventoryByWarehouseId_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake-id";

        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findAllByWarehouse_Id( fakeId ) ).thenReturn( Optional.empty() );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> inventoryService.getInventoryByWarehouseId( fakeId ) );

        assertThat( ex )
                .isNotNull()
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAllByWarehouse_Id( fakeId );
    }

    @Test
    void getInventoryByItemId_ShouldReturnInventoryList_WhenCalled() {
        //GIVEN
        List<InventoryResponseDTO> inventoryResponseDTO = mapper.toInventoryResponseDTOList( List.of( inventoryWithId ) );


        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findAllByItem_Id( fixedTestId ) ).thenReturn( Optional.of( List.of( inventoryWithId ) ) );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        List<InventoryResponseDTO> actual = assertDoesNotThrow( () -> inventoryService.getInventoryByItemId( fixedTestId ) );

        //THEN
        assertThat( actual )
                .isNotNull()
                .isEqualTo( inventoryResponseDTO );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAllByItem_Id( fixedTestId );
    }

    @Test
    void getInventoryByItemId_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake-id";

        Mockito.when( inventoryRepository.save( inventoryWithId ) ).thenReturn( inventoryWithId );
        Mockito.when( inventoryRepository.findAllByItem_Id( fakeId ) ).thenReturn( Optional.empty() );

        //WHEN
        inventoryRepository.save( inventoryWithId );

        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> inventoryService.getInventoryByItemId( fakeId ) );

        assertThat( ex )
                .isNotNull()
                .isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( inventoryWithId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findAllByItem_Id( fakeId );
    }

    @Test
    void createInventory_ShouldReturnInventory_WhenCalled() {
        //GIVEN
        InventoryCreateDTO inventoryCreateDTO = new InventoryCreateDTO(
                warehouse.getId(),
                item.getId(),
                2000
        );

        Inventory newInventory = mapper.toInventory( inventoryCreateDTO, warehouse, item );

        InventoryResponseDTO inventoryResponseDTO = mapper.toInventoryResponseDTO( newInventory );

        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );
        Mockito.when( itemRepo.findById( fixedTestId ) ).thenReturn( Optional.of( item ) );
        Mockito.when( inventoryRepository.insert( Mockito.any( Inventory.class ) ) ).thenReturn( newInventory );

        //WHEN
        InventoryResponseDTO actual = inventoryService.createInventory( inventoryCreateDTO );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "quantity" )
                .isEqualTo( inventoryCreateDTO.quantity() );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).insert( Mockito.any( Inventory.class ) );
        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        Mockito.verify( itemRepo, Mockito.times( 1 ) ).findById( fixedTestId );

    }

    @Test
    void updateInventory_ShouldReturnInventory_WhenCalled() {
        //GIVEN
        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO(
                warehouse.getId(),
                item.getId(),
                20123
        );

        Inventory updatedInventory = mapper.toInventory( inventory, inventoryUpdateDTO, warehouse, item );

        InventoryResponseDTO inventoryResponseDTO = mapper.toInventoryResponseDTO( updatedInventory );

        Mockito.when( inventoryRepository.insert( Mockito.any( Inventory.class ) ) ).thenReturn( inventory );
        Mockito.when( inventoryRepository.findById( fixedTestId ) ).thenReturn( Optional.of( inventory ) );

        Mockito.when( warehouseRepository.findById( fixedTestId ) ).thenReturn( Optional.of( warehouse ) );
        Mockito.when( itemRepo.findById( fixedTestId ) ).thenReturn( Optional.of( item ) );

        Mockito.when( inventoryRepository.save( Mockito.any( Inventory.class ) ) ).thenReturn( updatedInventory );

        inventoryRepository.insert( inventory );
        inventoryRepository.findById( fixedTestId );

        //WHEN
        InventoryResponseDTO actual = assertDoesNotThrow( () -> inventoryService.updateInventory( fixedTestId, inventoryUpdateDTO ) );

        //THEN
        assertThat( actual )
                .isNotNull()
                .extracting( "quantity" )
                .isEqualTo( inventoryUpdateDTO.quantity() );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).save( Mockito.any( Inventory.class ) );
        Mockito.verify( inventoryRepository, Mockito.times( 2 ) ).findById( fixedTestId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).insert( Mockito.any( Inventory.class ) );

        Mockito.verify( warehouseRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        Mockito.verify( itemRepo, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void updateInventory_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake-id";
        InventoryUpdateDTO inventoryUpdateDTO = new InventoryUpdateDTO(
                null,
                null,
                20123
        );

        Mockito.when( inventoryRepository.insert( Mockito.any( Inventory.class ) ) ).thenReturn( inventory );
        Mockito.when( inventoryRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        inventoryRepository.insert( inventory );

        //WHEN
        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () ->
                inventoryService.updateInventory( fakeId, inventoryUpdateDTO )
        );

        assertThat( ex ).isNotNull().isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).insert( inventory );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findById( fakeId );

    }

    @Test
    void deleteInventory_ShouldReturnTrue_WhenCalled() {
        //GIVEN

        Mockito.when( inventoryRepository.insert( Mockito.any( Inventory.class ) ) ).thenReturn( inventory );
        Mockito.when( inventoryRepository.findById( fixedTestId ) ).thenReturn( Optional.of( inventory ) );

        inventoryRepository.insert( inventory );

        //WHEN
        Boolean isDeleted = assertDoesNotThrow( () -> inventoryService.deleteInventory( fixedTestId ) );

        //THEN
        assertThat( isDeleted ).isTrue();

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).insert( Mockito.any( Inventory.class ) );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).delete( Mockito.any( Inventory.class ) );

    }

    @Test
    void deleteInventory_ShouldThrow_WhenCalled() {
        //GIVEN
        String fakeId = "fake-id";

        Mockito.when( inventoryRepository.insert( Mockito.any( Inventory.class ) ) ).thenReturn( inventory );
        Mockito.when( inventoryRepository.findById( fakeId ) ).thenReturn( Optional.empty() );

        inventoryRepository.insert( inventory );

        //WHEN
        ResourceNotFoundException ex = assertThrows( ResourceNotFoundException.class, () -> inventoryService.deleteInventory( fakeId ) );

        //THEN
        assertThat( ex ).isNotNull().isInstanceOf( ResourceNotFoundException.class );

        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).insert( Mockito.any( Inventory.class ) );
        Mockito.verify( inventoryRepository, Mockito.times( 1 ) ).findById( fakeId );
    }
}