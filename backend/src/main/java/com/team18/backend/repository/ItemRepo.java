package com.team18.backend.repository;

import com.team18.backend.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends MongoRepository<Item, String> {
}
