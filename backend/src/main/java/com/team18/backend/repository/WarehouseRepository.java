package com.team18.backend.repository;

import com.team18.backend.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
}
