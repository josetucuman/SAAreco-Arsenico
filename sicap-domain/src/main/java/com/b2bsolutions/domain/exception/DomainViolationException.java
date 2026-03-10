package com.b2bsolutions.domain.exception;

/**
 * Violación de una invariante técnica del dominio.
 * Ejemplo: concentración negativa, coordenadas fuera de rango.
 * HTTP: 422 Unprocessable Entity
 */
public class DomainViolationException extends RuntimeException {

    private final String errorCode;

    public DomainViolationException(String message) {
        super(message);
        this.errorCode = "DOMAIN_VIOLATION";
    }

    public DomainViolationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}