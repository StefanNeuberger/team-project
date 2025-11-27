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
    void run() throws Exception {

        assertDoesNotThrow( () -> seedCommands.seedAll() );
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


}