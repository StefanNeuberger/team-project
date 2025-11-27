package com.team18.backend.repository;

import com.team18.backend.model.Shipment;
import com.team18.backend.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    List<Shipment> findByWarehouse( Warehouse warehouse );

    List<Shipment> findAllByWarehouseIn( List<Warehouse> warehouses );
}

