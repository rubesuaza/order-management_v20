package com.example.management.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear o modificar una orden
 * con datos inválidos que violan los invariantes del dominio.
 */
public class InvalidOrderException extends DomainException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
}
