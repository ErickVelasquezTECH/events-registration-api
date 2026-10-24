package org.ies.tierno.api.model;

import java.time.LocalDateTime;

public record Event(Long id, String title, Category category, LocalDateTime startsAt, int capacity) {
}