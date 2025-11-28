package com.team18.backend.scripts;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestContainersConfiguration.class)
@SpringBootTest("spring.shell.interactive.enabled=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SeedCommandsTest {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private SeedCommands seedCommands;

    @Test
    void seed() {

        assertDoesNotThrow( () -> {
            assertEquals( "🌱 MongoDB has been seeded with new data.", seedCommands.seed() );
        } );
        //THEN
        long shopCount = shopRepository.count();
        long warehouseCount = warehouseRepository.count();
        long itemCount = itemRepo.count();
        long inventoryCount = inventoryRepository.count();
        long shipmentCount = shipmentRepository.count();

        assertEquals( 1, shopCount );
        assertEquals( 4, warehouseCount );
        assertEquals( 51, itemCount );
        assertEquals( 204, inventoryCount );
        assertEquals( 40, shipmentCount );

    }

    @Test
    void seed_2time() {
        assertDoesNotThrow( () -> {
            assertEquals( "🌱 MongoDB has been seeded with new data.", seedCommands.seed() );
            assertEquals( "✅ MongoDB already contains data. Skipping seeding.", seedCommands.seed() );
        } );
        //THEN
        long shopCount = shopRepository.count();
        long warehouseCount = warehouseRepository.count();
        long itemCount = itemRepo.count();
        long inventoryCount = inventoryRepository.count();
        long shipmentCount = shipmentRepository.count();

        assertEquals( 1, shopCount );
        assertEquals( 4, warehouseCount );
        assertEquals( 51, itemCount );
        assertEquals( 204, inventoryCount );
        assertEquals( 40, shipmentCount );

    }

    @Test
    void reset() {

        assertDoesNotThrow( () -> {
            assertEquals( "🌱 MongoDB has been seeded with new data.", seedCommands.seed() );
            assertEquals( "✅ MongoDB has been reset.", seedCommands.reset() );
        } );
        //THEN
        long shopCount = shopRepository.count();
        long warehouseCount = warehouseRepository.count();
        long itemCount = itemRepo.count();
        long inventoryCount = inventoryRepository.count();
        long shipmentCount = shipmentRepository.count();

        assertEquals( 0, shopCount );
        assertEquals( 0, warehouseCount );
        assertEquals( 0, itemCount );
        assertEquals( 0, inventoryCount );
        assertEquals( 0, shipmentCount );

    }


}