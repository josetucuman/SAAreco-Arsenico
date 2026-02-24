package com.b2bsolutions.domain.exception;

public class DomainViolationException extends RuntimeException{
    public DomainViolationException(String message) {
        super(message);
    }
}
