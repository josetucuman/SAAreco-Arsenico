package com.b2bsolutions.domain.exception;

public class ReglaNegocioException extends RuntimeException{
    private final String errorCode;

    public ReglaNegocioException(String message) {
        super(message);
        this.errorCode = "REGLA_NEGOCIO_VIOLATION";
    }

    public ReglaNegocioException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
