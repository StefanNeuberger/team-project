package com.team18.backend.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors for request bodies.
     * Returns a detailed error response with field-level validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions( MethodArgumentNotValidException ex ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach( ( error ) -> {
            String fieldName = ( ( FieldError ) error ).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put( fieldName, errorMessage );
        } );

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "Validation failed for one or more fields",
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                Instant.now(),
                errors
        );
        return new ResponseEntity<>( errorResponse, HttpStatus.BAD_REQUEST );
    }


    /**
     * Handle DuplicateKeyException - returns 409 Conflict
     * Occurs when trying to insert a document with a duplicate unique key
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException( DuplicateKeyException ex ) {
        String message = "A resource with this value already exists";
        String errorMsg = ex.getMessage();

        // Try to extract field name from MongoDB error message
        // Message format: "... dup key: { fieldName: \"value\" }"
        if ( errorMsg != null && errorMsg.contains( "dup key:" ) ) {
            try {
                int dupKeyIndex = errorMsg.indexOf( "dup key:" );
                int startBrace = errorMsg.indexOf( "{", dupKeyIndex );
                int endBrace = errorMsg.indexOf( "}", startBrace );

                if ( startBrace != -1 && endBrace != -1 ) {
                    String keyPart = errorMsg.substring( startBrace + 1, endBrace ).trim();
                    String fieldName = keyPart.split( ":" )[0].trim();
                    message = String.format( "A resource with this %s already exists", fieldName );
                }
            } catch ( Exception e ) {
                // If parsing fails, use generic message
            }
        }

        ErrorResponse errorResponse = new ErrorResponse(
                message,
                HttpStatus.CONFLICT.value(),
                "Conflict",
                Instant.now()
        );
        return new ResponseEntity<>( errorResponse, HttpStatus.CONFLICT );
    }

    /**
     * Handle ResourceNotFoundException - returns 404 Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException( ResourceNotFoundException ex ) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                Instant.now()
        );
        return new ResponseEntity<>( errorResponse, HttpStatus.NOT_FOUND );
    }

    /**
     * Handle generic runtime exceptions.
     * Returns a generic error response without exposing internal details.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException( RuntimeException ex ) {
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                Instant.now()
        );
        return new ResponseEntity<>( errorResponse, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    /**
     * Handle all other exceptions not caught by specific handlers.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException( Exception ex ) {
        ErrorResponse errorResponse = new ErrorResponse(
                "An error occurred while processing your request",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error",
                Instant.now()
        );
        return new ResponseEntity<>( errorResponse, HttpStatus.INTERNAL_SERVER_ERROR );
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleWarehouseNotFound( WarehouseNotFoundException ex ) {
        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                Instant.now()
        );
    }
}

