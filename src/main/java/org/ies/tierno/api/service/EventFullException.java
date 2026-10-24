package org.ies.tierno.api.service;

public class EventFullException extends ConflictException {

    public EventFullException(long eventId) {
        super("Event " + eventId + " is full");
    }
}