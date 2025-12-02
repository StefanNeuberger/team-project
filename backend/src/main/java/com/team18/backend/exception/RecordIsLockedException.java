package com.team18.backend.exception;

public class RecordIsLockedException extends RuntimeException {
    public RecordIsLockedException( String message ) {
        super( message );
    }
}
