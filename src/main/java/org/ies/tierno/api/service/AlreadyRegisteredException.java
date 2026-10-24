package org.ies.tierno.api.service;
public class AlreadyRegisteredException extends ConflictException {

    public AlreadyRegisteredException(long eventId, String email) {
        super(email + " is already registered in event " + eventId);
    }
}