package com.team18.backend.scripts;

import com.team18.backend.model.*;
import com.team18.backend.repository.*;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ShellComponent
public class SeedCommands {

    private final ShopRepository shopRepository;
    private final WarehouseRepository warehouseRepository;
    private final ShipmentRepository shipmentRepository;
    private final ItemRepo itemRepo;
    private final InventoryRepository inventoryRepository;
    private final ShipmentLineItemRepository shipmentLineItemRepository;

    public SeedCommands(
            ShopRepository shopRepository,
            WarehouseRepository warehouseRepository,
            ShipmentRepository shipmentRepository,
            ItemRepo itemRepo,
            InventoryRepository inventoryRepository,
            ShipmentLineItemRepository shipmentLineItemRepository
    ) {
        this.shopRepository = shopRepository;
        this.warehouseRepository = warehouseRepository;
        this.shipmentRepository = shipmentRepository;
        this.itemRepo = itemRepo;
        this.inventoryRepository = inventoryRepository;
        this.shipmentLineItemRepository = shipmentLineItemRepository;
    }

    @ShellMethod("Seeds the database with test data. It requires the database to be emtpy")
    public String seed() {
        long shopCount = shopRepository.count();
        long warehouseCount = warehouseRepository.count();
        long itemCount = itemRepo.count();
        long inventoryCount = inventoryRepository.count();
        long shipmentCount = shipmentRepository.count();
        long lineItemCount = shipmentLineItemRepository.count();

        if ( shopCount == 0 && warehouseCount == 0 && itemCount == 0 && inventoryCount == 0 && shipmentCount == 0 && lineItemCount == 0 ) {
            SecureRandom random = new SecureRandom();

            // Seed shop
            Shop shop = shopRepository.save(
                    new Shop(
                            "Kaufland"
                    )
            );

            Warehouse warehouseNorth = new Warehouse(
                    "Warehouse EU North",
                    shop,
                    10.1999836,
                    54.3187386,
                    "Benzstraße",
                    "1",
                    "Kiel",
                    "24148",
                    "Schleswig-Holstein",
                    "Deutschland",
                    270_000
            );

            Warehouse warehouseEast = new Warehouse(
                    "Warehouse EU East",
                    shop,
                    14.5049545,
                    52.3415954,
                    "Nuhnenstraße",
                    "19",
                    "Frankfurt (Oder)",
                    "15234",
                    "Brandenburg",
                    "Deutschland",
                    135_000
            );

            Warehouse warehouseSouth = new Warehouse(
                    "Warehouse EU South",
                    shop,
                    11.6068645,
                    48.196789,
                    "Maria-Probst-Straße",
                    "50",
                    "München",
                    "80939",
                    "Bayern",
                    "Deutschland",
                    547_500
            );

            Warehouse warehouseWest = new Warehouse(
                    "Warehouse EU West",
                    shop,
                    7.0016891,
                    50.9194582,
                    "Poller Holzweg",
                    "9",
                    "Köln",
                    "51105",
                    "Nordrhein-Westfalen",
                    "Deutschland",
                    489_000
            );

            List<Warehouse> warehouses = warehouseRepository.saveAll(
                    List.of(
                            warehouseNorth,
                            warehouseEast,
                            warehouseSouth,
                            warehouseWest
                    )
            );

            // Seed items
            List<Item> items = itemRepo.saveAll(
                    List.of(
                            new Item(
                                    null,
                                    "KL-MED-15",
                                    "K-Classic Mineralwasser Medium"
                            ),
                            new Item(
                                    null,
                                    "KL-NMG-01",
                                    "K-Classic Nudel Mix Gabelspaghetti 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-BRT-03",
                                    "K-Classic Graubrot Geschnitten 750g"
                            ),
                            new Item(
                                    null,
                                    "KL-RIS-07",
                                    "K-Classic Langkornreis 1kg"
                            ),
                            new Item(
                                    null,
                                    "KL-ZCK-08",
                                    "K-Classic Haushaltszucker 1kg"
                            ),
                            new Item(
                                    null,
                                    "KL-SLT-09",
                                    "K-Classic Feines Speisesalz 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-OLI-10",
                                    "K-Classic Natives Olivenöl Extra 1L"
                            ),
                            new Item(
                                    null,
                                    "KL-KCH-11",
                                    "K-Classic Ketchup 1L"
                            ),
                            new Item(
                                    null,
                                    "KL-PST-13",
                                    "K-Classic Fusilli Pasta 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-HFN-14",
                                    "K-Classic Haferflocken Zart 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-MSN-16",
                                    "K-Classic Multivitaminsaft 1L"
                            ),
                            new Item(
                                    null,
                                    "KL-APP-17",
                                    "K-Classic Apfelmus 720ml"
                            ),
                            new Item(
                                    null,
                                    "KL-BHN-18",
                                    "K-Classic Kidneybohnen 400g"
                            ),
                            new Item(
                                    null,
                                    "KL-TMT-19",
                                    "K-Classic Passierte Tomaten 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-KFL-20",
                                    "K-Classic Kartoffeln Speisefrühkartoffeln 2,5kg"
                            ),
                            new Item(
                                    null,
                                    "KL-CHP-21",
                                    "K-Classic Paprika Chips 200g"
                            ),
                            new Item(
                                    null,
                                    "KL-ICE-22",
                                    "K-Classic Vanilleeis Family Pack 1,5L"
                            ),
                            new Item(
                                    null,
                                    "KL-JGH-23",
                                    "K-Classic Joghurt Erdbeere 150g"
                            ),
                            new Item(
                                    null,
                                    "KL-MSM-24",
                                    "K-Classic Mischgemüse Tiefkühl 1kg"
                            ),
                            new Item(
                                    null,
                                    "KL-BHN-26",
                                    "K-Classic Baked Beans 415g"
                            ),
                            new Item(
                                    null,
                                    "KL-CFL-27",
                                    "K-Classic Cornflakes 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-NLG-28",
                                    "K-Classic Nougat Bits 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-ORG-29",
                                    "K-Classic Orangensaft 1L"
                            ),
                            new Item(
                                    null,
                                    "KL-MIN-30",
                                    "K-Classic Minipizzen Tiefkühl 9 Stück"
                            ),
                            new Item(
                                    null,
                                    "KL-PSN-32",
                                    "K-Classic Pilsener Bier 0,5L"
                            ),
                            new Item(
                                    null,
                                    "KL-SPG-33",
                                    "K-Classic Spaghetti 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-KKS-35",
                                    "K-Classic Knäckebrot Sesam 250g"
                            ),
                            new Item(
                                    null,
                                    "KL-RST-36",
                                    "K-Classic Röstzwiebeln 150g"
                            ),
                            new Item(
                                    null,
                                    "KL-BTK-37",
                                    "K-Classic Blätterteig Frisch 275g"
                            ),
                            new Item(
                                    null,
                                    "KL-MEO-38",
                                    "K-Classic Meersalz Fein 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-HON-39",
                                    "K-Classic Blütenhonig 500g"
                            ),
                            new Item(
                                    null,
                                    "KL-KFF-40",
                                    "K-Classic Kaffee Gold Röstung 500g"
                            ),
                            new Item(
                                    null,
                                    "BR-PSG-41",
                                    "Barilla Spaghetti No. 5 500g"
                            ),
                            new Item(
                                    null,
                                    "CC-CLS-42",
                                    "Coca-Cola Classic 1,25L"
                            ),
                            new Item(
                                    null,
                                    "HR-GMI-43",
                                    "Haribo Goldbären 200g"
                            ),
                            new Item(
                                    null,
                                    "ML-JGH-44",
                                    "Müller Joghurt mit der Ecke Erdbeer 150g"
                            ),
                            new Item(
                                    null,
                                    "NE-TRB-45",
                                    "Nestlé Träubles Frühstücksflocken 375g"
                            ),
                            new Item(
                                    null,
                                    "DM-OLE-46",
                                    "De Cecco Olivenöl Extra Vergine 1L"
                            ),
                            new Item(
                                    null,
                                    "OR-JUC-47",
                                    "Granini Orange Direktsaft 1L"
                            ),
                            new Item(
                                    null,
                                    "TM-PZZ-48",
                                    "Dr. Oetker Ristorante Pizza Mozzarella 330g"
                            ),
                            new Item(
                                    null,
                                    "MD-MLK-49",
                                    "Milka Alpenmilch Schokolade 100g"
                            ),
                            new Item(
                                    null,
                                    "LR-BUT-50",
                                    "Kerrygold Original Irische Butter 250g"
                            ),
                            new Item(
                                    null,
                                    "KN-CHP-51",
                                    "Pringles Sour Cream & Onion 200g"
                            ),
                            new Item(
                                    null,
                                    "BR-PST-52",
                                    "Barilla Penne Rigate 500g"
                            ),
                            new Item(
                                    null,
                                    "RB-ENR-53",
                                    "Red Bull Energy Drink 250ml"
                            ),
                            new Item(
                                    null,
                                    "MG-KSE-54",
                                    "Meggle Kräuterbutter 100g"
                            ),
                            new Item(
                                    null,
                                    "BE-MSN-55",
                                    "Beck's Pils 0,5L"
                            ),
                            new Item(
                                    null,
                                    "OR-TNA-56",
                                    "Oro di Parma Tomatenmark 200g"
                            ),
                            new Item(
                                    null,
                                    "KP-CFE-58",
                                    "Kellogg’s Corn Flakes 500g"
                            ),
                            new Item(
                                    null,
                                    "BL-ICE-59",
                                    "Ben & Jerry’s Cookie Dough 465ml"
                            ),
                            new Item(
                                    null,
                                    "LN-RIS-60",
                                    "Uncle Ben’s Risotto Reis 500g"
                            )
                    )
            );

//            inventoryRepository.saveAll(
//                    items.stream().flatMap( item -> warehouses.stream().map( warehouse -> {
//                                int quantity = 20 + random.nextInt( 2000 - 20 + 1 );
//                                return new Inventory(
//                                        warehouse,
//                                        item,
//                                        quantity
//                                );
//                            } )
//                    ).toList()
//            );
//
//            // Seed Shipments
//            LocalDate now = LocalDate.now();
//            LocalDate start = now.minusMonths( 1 );
//            LocalDate end = now.plusMonths( 3 );
//
//            long startEpoch = start.toEpochDay();
//            long endEpoch = end.toEpochDay();
//
//            Map<Integer, ShipmentStatus> statusMap = Map.of(
//                    0, ShipmentStatus.ORDERED,
//                    1, ShipmentStatus.PROCESSED,
//                    2, ShipmentStatus.IN_DELIVERY,
//                    3, ShipmentStatus.COMPLETED
//            );
//
//            List<Shipment> shipments = shipmentRepository.saveAll(
//                    warehouses.stream().flatMap( warehouse -> {
//                        List<Shipment> shipmentList = new ArrayList<>();
//                        for ( int i = 0; i < 10; i++ ) {
//                            long randomDay = startEpoch + Math.absExact( random.nextLong() ) % ( endEpoch - startEpoch + 1 );
//                            int status = random.nextInt( 4 );
//                            shipmentList.add(
//                                    new Shipment(
//                                            warehouse,
//                                            LocalDate.ofEpochDay( randomDay ),
//                                            statusMap.get( status )
//                                    )
//                            );
//                        }
//                        return shipmentList.stream();
//                    } ).toList()
//            );
//
//            int bound = items.size() - 1;
//
//            shipmentLineItemRepository.saveAll(
//                    shipments.stream().flatMap( shipment -> {
//                        List<ShipmentLineItem> lineItemList = new ArrayList<>();
//                        for ( int i = 0; i < 10; i++ ) {
//                            int randomItem = random.nextInt( bound );
//                            int quantity = random.nextInt( 500 );
//                            Item item = items.get( randomItem );
//                            lineItemList.add(
//                                    new ShipmentLineItem(
//                                            shipment,
//                                            item,
//                                            quantity,
//                                            quantity
//                                    )
//                            );
//                        }
//                        return lineItemList.stream();
//                    } ).toList()
//            );

            return "🌱 MongoDB has been seeded with new data.";
        } else {
            return "✅ MongoDB already contains data. Skipping seeding.";
        }
    }

    @ShellMethod("Resets the database by erasing all data")
    public String reset() {
        if ( this.shipmentRepository.count() != 0 ) {
            this.shipmentRepository.deleteAll();
        }
        if ( this.inventoryRepository.count() != 0 ) {
            this.inventoryRepository.deleteAll();
        }
        if ( this.itemRepo.count() != 0 ) {
            this.itemRepo.deleteAll();
        }
        if ( this.warehouseRepository.count() != 0 ) {
            this.warehouseRepository.deleteAll();
        }
        if ( this.shopRepository.count() != 0 ) {
            this.shopRepository.deleteAll();
        }
        if ( this.shipmentLineItemRepository.count() != 0 ) {
            this.shipmentLineItemRepository.deleteAll();
        }

        return "✅ MongoDB has been reset.";
    }
}
