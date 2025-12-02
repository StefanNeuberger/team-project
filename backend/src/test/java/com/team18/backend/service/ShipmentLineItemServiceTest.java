package com.team18.backend.service;

import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemCreateDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemMapper;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemResponseDTO;
import com.team18.backend.dto.shipmentlineitem.ShipmentLineItemUpdateDTO;
import com.team18.backend.exception.RecordIsLockedException;
import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.*;
import com.team18.backend.repository.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentLineItemServiceTest {

    private static final ItemRepo itemRepo = Mockito.mock( ItemRepo.class );
    private static final ShipmentRepository shipmentRepository = Mockito.mock( ShipmentRepository.class );
    private static final ShipmentLineItemRepository shipmentLineItemRepository = Mockito.mock( ShipmentLineItemRepository.class );
    private static final ShipmentLineItemMapper mapper = new ShipmentLineItemMapper();


    private static final ShipmentLineItemService service = new ShipmentLineItemService(
            mapper,
            shipmentRepository,
            itemRepo,
            shipmentLineItemRepository
    );

    private static final String fixedTestId = "test-id";
    private static Warehouse warehouse;
    private static Item item;
    private static Shipment shipment;
    private static ShipmentLineItem shipmentLineItem;

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

        shipment = new Shipment(
                fixedTestId,
                warehouse,
                LocalDate.now(),
                ShipmentStatus.ORDERED
        );

        shipmentLineItem = new ShipmentLineItem(
                fixedTestId,
                shipment,
                item,
                10,
                10
        );

    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset( itemRepo, shipmentRepository, shipmentLineItemRepository );
        shipment.setStatus( ShipmentStatus.ORDERED );
    }

    @Test
    void getShipmentLineItemsByShipmentId_shouldReturnAllItems_whenRepositoryHasItems() {
        // GIVEN

        List<ShipmentLineItem> given = List.of( shipmentLineItem );
        List<ShipmentLineItemResponseDTO> expected = mapper.toResponseDTOList( given );
        when( shipmentLineItemRepository.findAllByShipment_Id( fixedTestId ) ).thenReturn( Optional.of( given ) );

        // WHEN
        List<ShipmentLineItemResponseDTO> actualItems = service.getShipmentLineItemsByShipmentId( fixedTestId );

        // THEN
        assertThat( actualItems ).hasSize( 1 );
        assertThat( actualItems ).containsAll( expected );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findAllByShipment_Id( fixedTestId );
    }

    @Test
    void getShipmentLineItemsByShipmentId_shouldThrow_whenRepositoryHasNoItems() {
        // GIVEN
        List<ShipmentLineItem> given = List.of();
        when( shipmentLineItemRepository.findAllByShipment_Id( fixedTestId ) ).thenReturn( Optional.of( given ) );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .getShipmentLineItemsByShipmentId( fixedTestId )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findAllByShipment_Id( fixedTestId );
    }

    @Test
    void createShipmentLineItem_shouldThrow_whenShipmentNotFound() {
        // GIVEN
        ShipmentLineItemCreateDTO newLineItem = new ShipmentLineItemCreateDTO(
                "wrong-id",
                item.getId(),
                10,
                10
        );
        when( shipmentRepository.findById( "wrong-id" ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .createShipmentLineItem( newLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentRepository, Mockito.times( 1 ) ).findById( "wrong-id" );
    }

    @Test
    void createShipmentLineItem_shouldThrow_whenItemNotFound() {
        // GIVEN
        ShipmentLineItemCreateDTO newLineItem = new ShipmentLineItemCreateDTO(
                fixedTestId,
                "wrong-id",
                10,
                10
        );
        when( shipmentRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipment ) );
        when( itemRepo.findById( "wrong-id" ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .createShipmentLineItem( newLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( itemRepo, Mockito.times( 1 ) ).findById( "wrong-id" );
    }

    @Test
    void createShipmentLineItem_shouldReturnNewLineItem_whenCalled() {
        // GIVEN
        ShipmentLineItemCreateDTO newLineItem = new ShipmentLineItemCreateDTO(
                fixedTestId,
                fixedTestId,
                10,
                10
        );
        ShipmentLineItem given = mapper.toShipmentLineItem( newLineItem, shipment, item );
        ShipmentLineItemResponseDTO expected = mapper.toResponseDTO( given );

        when( shipmentRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipment ) );
        when( itemRepo.findById( fixedTestId ) ).thenReturn( Optional.of( item ) );
        when( shipmentLineItemRepository.insert( Mockito.any( ShipmentLineItem.class ) ) ).thenReturn( given );

        // WHEN
        ShipmentLineItemResponseDTO actual = service.createShipmentLineItem( newLineItem );

        // THEN
        assertThat( actual )
                .isNotNull()
                .isEqualTo( expected );

        verify( shipmentRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( itemRepo, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).insert( Mockito.any( ShipmentLineItem.class ) );
    }

    @Test
    void updateShipmentLineItem_shouldThrow_whenShipmentLineItemNotFound() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                null,
                null,
                null,
                null
        );
        when( shipmentLineItemRepository.findById( "wrong-id" ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .updateShipmentLineItem( "wrong-id", updatedLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( "wrong-id" );
    }

    @Test
    void updateShipmentLineItem_shouldThrow_whenShipmentIsLocked() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                null,
                null,
                null,
                null
        );
        shipment.setStatus( ShipmentStatus.COMPLETED );

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );

        // WHEN
        RecordIsLockedException e = assertThrows( RecordIsLockedException.class, () -> service
                .updateShipmentLineItem( fixedTestId, updatedLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( RecordIsLockedException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void updateShipmentLineItem_shouldThrow_whenShipmentNotFound() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                "wrong-id",
                null,
                null,
                null
        );

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );
        when( shipmentRepository.findById( updatedLineItem.shipmentId() ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .updateShipmentLineItem( fixedTestId, updatedLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentRepository, Mockito.times( 1 ) ).findById( updatedLineItem.shipmentId() );
    }

    @Test
    void updateShipmentLineItem_shouldThrow_whenItemNotFound() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                shipment.getId(),
                "wrong-id",
                null,
                null
        );
        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );
        when( shipmentRepository.findById( updatedLineItem.shipmentId() ) ).thenReturn( Optional.of( shipment ) );
        when( itemRepo.findById( updatedLineItem.itemId() ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .updateShipmentLineItem( fixedTestId, updatedLineItem )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentRepository, Mockito.times( 1 ) ).findById( updatedLineItem.shipmentId() );
        verify( itemRepo, Mockito.times( 1 ) ).findById( updatedLineItem.itemId() );
    }

    @Test
    void updateShipmentLineItem_shouldReturnShipmenLineItem_whenReferencesAreNull() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                null,
                null,
                null,
                null
        );
        ShipmentLineItem given = mapper.toShipmentLineItem( shipmentLineItem, updatedLineItem, shipment, item );
        ShipmentLineItemResponseDTO expected = mapper.toResponseDTO( given );

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );
        when( shipmentLineItemRepository.save( Mockito.any( ShipmentLineItem.class ) ) ).thenReturn( given );

        // WHEN
        ShipmentLineItemResponseDTO actual = assertDoesNotThrow( () -> service.updateShipmentLineItem( fixedTestId, updatedLineItem ) );

        // THEN
        assertThat( actual ).isNotNull().isEqualTo( expected );

        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).save( given );
    }

    @Test
    void updateShipmentLineItem_shouldReturnShipmenLineItem_whenReferencesAreNotNull() {
        // GIVEN
        ShipmentLineItemUpdateDTO updatedLineItem = new ShipmentLineItemUpdateDTO(
                shipment.getId(),
                item.getId(),
                100,
                100
        );
        ShipmentLineItem given = mapper.toShipmentLineItem( shipmentLineItem, updatedLineItem, shipment, item );
        ShipmentLineItemResponseDTO expected = mapper.toResponseDTO( given );

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );
        when( shipmentRepository.findById( updatedLineItem.shipmentId() ) ).thenReturn( Optional.of( shipment ) );
        when( itemRepo.findById( updatedLineItem.itemId() ) ).thenReturn( Optional.of( item ) );
        when( shipmentLineItemRepository.save( Mockito.any( ShipmentLineItem.class ) ) ).thenReturn( given );

        // WHEN
        ShipmentLineItemResponseDTO actual = assertDoesNotThrow( () -> service.updateShipmentLineItem( fixedTestId, updatedLineItem ) );

        // THEN
        assertThat( actual ).isNotNull().isEqualTo( expected );

        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( itemRepo, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).save( given );
    }

    @Test
    void deleteShipmentLineItem_shouldThrow_whenShipmentLineItemNotFound() {
        // GIVEN

        when( shipmentLineItemRepository.findById( "wrong-id" ) ).thenReturn( Optional.empty() );

        // WHEN
        ResourceNotFoundException e = assertThrows( ResourceNotFoundException.class, () -> service
                .deleteShipmentLineItem( "wrong-id" )
        );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( ResourceNotFoundException.class );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( "wrong-id" );
    }

    @Test
    void deleteShipmentLineItem_shouldThrow_WhenShipmentIsLocked() {
        // GIVEN
        shipment.setStatus( ShipmentStatus.COMPLETED );

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );

        // WHEN
        RecordIsLockedException e = assertThrows( RecordIsLockedException.class, () -> service.deleteShipmentLineItem( fixedTestId ) );

        // THEN
        assertThat( e ).isNotNull().isInstanceOf( RecordIsLockedException.class );

        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
    }

    @Test
    void deleteShipmentLineItem_shouldReturnTrue_WhenCalledWithValidId() {
        // GIVEN

        when( shipmentLineItemRepository.findById( fixedTestId ) ).thenReturn( Optional.of( shipmentLineItem ) );

        // WHEN
        Boolean actual = assertDoesNotThrow( () -> service.deleteShipmentLineItem( fixedTestId ) );

        // THEN
        assertTrue( actual );

        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).findById( fixedTestId );
        verify( shipmentLineItemRepository, Mockito.times( 1 ) ).delete( shipmentLineItem );
    }

}