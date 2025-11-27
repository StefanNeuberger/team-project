package com.team18.backend.repository;

import com.team18.backend.model.Shop;
import com.team18.backend.model.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
    List<Warehouse> findByShop( Shop shop );
}
