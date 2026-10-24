package org.ies.tierno.api.service;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }
}