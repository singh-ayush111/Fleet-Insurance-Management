package com.htc.fleetmanagement.exception;

public class CorporateClientNotFound extends RuntimeException {
    
    public CorporateClientNotFound(String message) {
        super(message);
    }
    
    public CorporateClientNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
