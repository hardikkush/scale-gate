package com.scalegate.scale_gate.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVehicle(
            DuplicateVehicleException exception) {

        Map<String, String> errorResponse = Map.of(
                "message", exception.getMessage(),
                "status", "409 CONFLICT"
        );

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleVehicleNotFound(
            VehicleNotFoundException exception) {

        Map<String, String> errorResponse = Map.of(
                "message", exception.getMessage(),
                "status", "404 NOT_FOUND"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

}