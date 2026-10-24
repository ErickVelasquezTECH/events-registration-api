package org.ies.tierno.api.service;

import org.ies.tierno.api.controller.dto.EventRequest;
import org.ies.tierno.api.controller.dto.RegistrationRequest;
import org.ies.tierno.api.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventService {

    private final Map<Long, Event> events = new ConcurrentHashMap<>();
    private final Map<Long, Registration> registrations = new ConcurrentHashMap<>();
    private final AtomicLong nextEventId = new AtomicLong(1);
    private final AtomicLong nextRegistrationId = new AtomicLong(1);

    public EventService() {
        createEvent(new EventRequest("Introducción a Spring Boot", Category.TALK, daysFromNow(20, 17), 50));
        Event docker = createEvent(new EventRequest("Taller de Docker", Category.WORKSHOP, daysFromNow(27, 16), 2));
        createEvent(new EventRequest("Hackathon PSP", Category.HACKATHON, daysFromNow(56, 9), 100));
        register(docker.id(), new RegistrationRequest("Ana García", "ana@example.com"));
        register(docker.id(), new RegistrationRequest("Luis Pérez", "luis@example.com"));
    }

    /**
     * Filtra por categoría y por el rango de fechas [from, to], ambos inclusive.
     * Los filtros null no se aplican. El resultado se ordena por fecha de inicio.
     */
    public List<Event> searchEvents(Category category, LocalDate from, LocalDate to) {
        return events.values().stream()
                .filter(event -> category == null || event.category() == category)
                .filter(event -> from == null || !event.startsAt().toLocalDate().isBefore(from))
                .filter(event -> to == null || !event.startsAt().toLocalDate().isAfter(to))
                .sorted(Comparator.comparing(Event::startsAt))
                .toList();
    }

    /**
     * @throws EventNotFoundException
     */
    public Event findEvent(long eventId) {
        Event event = events.get(eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId);
        }
        return event;
    }

    public synchronized Event createEvent(EventRequest request) {
        long id = nextEventId.getAndIncrement();
        Event event = new Event(id, request.title(), request.category(), request.startsAt(), request.capacity());
        events.put(id, event);
        return event;
    }

    /**
     * Actualización parcial: los campos null de la petición no se modifican.
     *
     * @throws EventNotFoundException
     * @throws CapacityBelowRegistrationsException si la nueva capacidad es menor
     *                                             que el número de inscritos
     */

    /**
     * Elimina el evento y todas sus inscripciones.
     *
     * @throws EventNotFoundException
     */
    public synchronized void deleteEvent(long eventId) {
        findEvent(eventId);
        registrations.values().removeIf(registration -> registration.eventId() == eventId);
        events.remove(eventId);
    }

    /**
     * @throws EventNotFoundException
     */
    public List<Registration> findRegistrations(long eventId) {
        findEvent(eventId);
        return registrations.values().stream()
                .filter(registration -> registration.eventId() == eventId)
                .sorted(Comparator.comparing(Registration::id))
                .toList();
    }

    /**
     * @throws EventNotFoundException
     * @throws RegistrationNotFoundException si la inscripción no existe o es de otro evento
     */
    public Registration findRegistration(long eventId, long registrationId) {
        findEvent(eventId);
        Registration registration = registrations.get(registrationId);
        if (registration == null || registration.eventId() != eventId) {
            throw new RegistrationNotFoundException(eventId, registrationId);
        }
        return registration;
    }

    /**
     * @throws EventNotFoundException
     * @throws AlreadyRegisteredException si ese email ya está inscrito en el evento
     * @throws EventFullException         si no quedan plazas
     */
    public synchronized Registration register(long eventId, RegistrationRequest request) {
        Event event = findEvent(eventId);
        boolean alreadyRegistered = registrations.values().stream()
                .anyMatch(registration -> registration.eventId() == eventId
                        && registration.attendeeEmail().equalsIgnoreCase(request.attendeeEmail()));
        if (alreadyRegistered) {
            throw new AlreadyRegisteredException(eventId, request.attendeeEmail());
        }
        if (countRegistrations(eventId) >= event.capacity()) {
            throw new EventFullException(eventId);
        }
        long id = nextRegistrationId.getAndIncrement();
        Registration registration = new Registration(id, eventId, request.attendeeName(),
                request.attendeeEmail(), LocalDateTime.now());
        registrations.put(id, registration);
        return registration;
    }

    /**
     * @throws EventNotFoundException
     * @throws RegistrationNotFoundException
     */
    public synchronized void cancelRegistration(long eventId, long registrationId) {
        findRegistration(eventId, registrationId);
        registrations.remove(registrationId);
    }

    private long countRegistrations(long eventId) {
        return registrations.values().stream()
                .filter(registration -> registration.eventId() == eventId)
                .count();
    }

    private static LocalDateTime daysFromNow(int days, int hour) {
        return LocalDate.now().plusDays(days).atTime(hour, 0);
    }
}