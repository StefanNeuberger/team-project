package com.team18.backend.service;

import com.team18.backend.exception.ResourceNotFoundException;
import com.team18.backend.model.Shop;
import com.team18.backend.repository.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private final ShopRepository repository;

    public ShopService( ShopRepository repository ) {
        this.repository = repository;
    }

    public List<Shop> getAllShops() {
        return repository.findAll();
    }

    public Shop getShopById( String id ) {
        return repository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "Shop not found with id: " + id ) );
    }

    public Shop createShop( String name ) {
        return repository.save( new Shop( name ) );
    }
}

