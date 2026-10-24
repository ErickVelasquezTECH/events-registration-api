package org.ies.tierno.api.model;

import java.time.LocalDateTime;

public record Registration(Long id, Long eventId, String attendeeName, String attendeeEmail,
                           LocalDateTime registeredAt) {
}