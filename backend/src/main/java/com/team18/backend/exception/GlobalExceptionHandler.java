package com.team18.backend.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors - returns 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        FieldValidationErrorResponse errorResponse = new FieldValidationErrorResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                Instant.now(),
                fieldErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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

