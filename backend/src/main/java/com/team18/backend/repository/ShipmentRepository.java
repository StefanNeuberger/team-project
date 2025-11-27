package com.team18.backend.repository;

import com.team18.backend.model.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    List<Shipment> findByWarehouseId( String warehouseId );

    List<Shipment> findAllByWarehouseIdIn( List<String> warehouseIds );
}

