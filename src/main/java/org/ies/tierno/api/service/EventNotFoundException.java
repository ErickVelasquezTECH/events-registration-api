package org.ies.tierno.api.service;

public class EventNotFoundException extends NotFoundException {

    public EventNotFoundException(long eventId) {
        super("Event " + eventId + " not found");
    }
}