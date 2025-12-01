package com.team18.backend.repository;

import com.team18.backend.model.ShipmentLineItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentLineItemRepository extends MongoRepository<ShipmentLineItem, String> {
    Optional<List<ShipmentLineItem>> findAllByShipment_Id( String shipmentId );
}
