package com.team18.backend.exception;

public class WarehouseNotFoundException extends Exception {
    public WarehouseNotFoundException( String id ) {
        super( "Could not find warehouse with id: " + id );
    }
}
