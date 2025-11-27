package com.team18.backend.scripts;

import com.team18.backend.TestContainersConfiguration;
import com.team18.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "team18.data-seed.enabled=true")
@Import(TestContainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DataSeedTest {

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


    @Test
    void run() throws Exception {
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