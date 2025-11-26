package com.team18.backend.repository;

import com.team18.backend.model.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ShipmentRepository extends MongoRepository<Shipment, String> {
    
    List<Shipment> findByWarehouseId(String warehouseId);
    
    @Query("{'warehouseId': {'$in': ?0}}")
    List<Shipment> findAllByWarehouseIdIn(List<String> warehouseIds);
}

