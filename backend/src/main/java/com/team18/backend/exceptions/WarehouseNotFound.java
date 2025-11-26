package com.team18.backend.exceptions;

public class WarehouseNotFound extends Exception {
    public WarehouseNotFound( String id ) {
        super( "Could not find warehouse with id: " + id );
    }
}
