package com.team18.backend.service;

import com.team18.backend.model.Item;
import com.team18.backend.repository.ItemRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceTest {

    ItemRepo mockedItemRepo;
    ItemService mockedItemService;

    @BeforeEach
    void setUp() {
        mockedItemRepo = Mockito.mock( ItemRepo.class );
        mockedItemService = new ItemService( mockedItemRepo );
    }

    @Nested
    class FindItemById {
        @Test
        @DisplayName("Should return item when found by id")
        void findItemById() {
            String itemId = "1";
            Item expectedItem = new Item( itemId, "SKU123", "Test Item" );

            when( mockedItemRepo.findById( itemId ) ).thenReturn( Optional.of( expectedItem ) );

            Item actualItem = mockedItemService.findItemById( itemId );

            assertEquals( expectedItem, actualItem );
        }

        @Test
        @DisplayName("Should throw error when item not found by id")
        void findItemByIdNotFound() {
            String notExistingItemId = "999";

            when( mockedItemRepo.findById( notExistingItemId ) ).thenReturn( Optional.empty() );
        }
    }

    @Nested
    class updateItemByIdTests {
        @Test
        @DisplayName("Should return item when updated successfully")
        void updateItemById() {
            String itemId = "1";
            Item existingItem = new Item( itemId, "SKU123", "Old Name" );
            Item updatedItem = new Item( itemId, "SKU123", "New Name" );

            when( mockedItemRepo.findById( itemId ) ).thenReturn( Optional.of( existingItem ) );
            when( mockedItemRepo.save( existingItem ) ).thenReturn( updatedItem );

            Item actualItem = mockedItemService.updateItemById( itemId, updatedItem );

            assertEquals( updatedItem, actualItem );
            verify( mockedItemRepo ).save( existingItem );
        }

        @Test
        @DisplayName("Should throw exception when item not found")
        void updateItemByIdNotFound() {
            String notExistingItemId = "999";
            Item updatedItem = new Item( notExistingItemId, "SKU999", "New Name" );

            when( mockedItemRepo.findById( notExistingItemId ) ).thenReturn( Optional.empty() );

            assertThrows( RuntimeException.class, () ->
                    mockedItemService.updateItemById( notExistingItemId, updatedItem ) );
        }
    }

    @Nested
    class deleteAllItemTests {
        @Test
        @DisplayName("Should return true when all items deleted")
        void deleteAllItems() {
            boolean isDeleted = mockedItemService.deleteAllItems();

            assertTrue( isDeleted );
            verify( mockedItemRepo ).deleteAll();
        }
    }

    @Nested
    class deleteItemTests {
        @Test
        @DisplayName("Should return true when item deleted by id")
        void deleteItem() {
            String itemId = "1";
            Item existingItem = new Item( itemId, "SKU123", "Test Item" );

            when( mockedItemRepo.findById( itemId ) ).thenReturn( Optional.of( existingItem ) );

            boolean isDeleted = mockedItemService.deleteItemById( itemId );

            assertTrue( isDeleted );
            verify( mockedItemRepo ).delete( existingItem );
        }

        @Test
        @DisplayName("Should return exception when item not found")
        void deleteItemNotFound() {
            String notExistingItemId = "999";

            when( mockedItemRepo.findById( notExistingItemId ) ).thenReturn( Optional.empty() );

            assertThrows( RuntimeException.class, () ->
                    mockedItemService.deleteItemById( notExistingItemId ) );
        }
    }

    @Nested
    class createItemTests {
        @Test
        @DisplayName("Should return newly created item")
        void createItem() {
            Item newItem = new Item( "1", "SKU123", "Test Item" );

            when( mockedItemRepo.save( newItem ) ).thenReturn( new Item( "1", "SKU123", "Test Item" ) );

            Item actualItem = mockedItemService.createItem( newItem );

            assertEquals( newItem, actualItem );
            verify( mockedItemRepo ).save( newItem );
        }

    }
}