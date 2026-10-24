package org.ies.tierno.api.service;

public class RegistrationNotFoundException extends NotFoundException {

    public RegistrationNotFoundException(long eventId, long registrationId) {
        super("Registration " + registrationId + " not found for event " + eventId);
    }
}