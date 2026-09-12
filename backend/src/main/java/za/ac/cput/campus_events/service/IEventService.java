package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.Event;

public interface IEventService {
    // Register a student for an event
    Event registerStudent(Long eventId);

    // Cancel an event
    Event cancelEvent(Long eventId);

    void forceCancelEvent(Long id, Long adminId);
}

