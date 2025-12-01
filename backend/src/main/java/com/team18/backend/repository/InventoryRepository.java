package com.team18.backend.repository;

import com.team18.backend.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
    Optional<List<Inventory>> findAllByWarehouse_Id( String warehouseId );

    Optional<List<Inventory>> findAllByItem_Id( String itemId );
}
