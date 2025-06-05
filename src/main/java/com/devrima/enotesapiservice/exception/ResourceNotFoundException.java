package com.devrima.enotesapiservice.exception;

import java.util.function.Supplier;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(String message) {
        super ( message );
    }
}
