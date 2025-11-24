package com.a3seguranca.mfademo.exception;

public class InvalidMfaCodeException extends RuntimeException {
    public InvalidMfaCodeException(String message) {
        super(message);
    }
    
    public InvalidMfaCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
