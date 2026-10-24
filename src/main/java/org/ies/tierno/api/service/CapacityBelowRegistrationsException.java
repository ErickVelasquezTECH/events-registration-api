package org.ies.tierno.api.service;

public class CapacityBelowRegistrationsException extends ConflictException {

    public CapacityBelowRegistrationsException(long eventId, int capacity, long registered) {
        super("Event " + eventId + " has " + registered + " registrations, capacity cannot be " + capacity);
    }
}
