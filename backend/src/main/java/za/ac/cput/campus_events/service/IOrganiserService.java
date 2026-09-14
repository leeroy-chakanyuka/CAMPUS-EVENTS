package za.ac.cput.campus_events.service;
/*
Mologadi Dikgale
Student No: 231016263
 */

import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Event;
import java.util.List;
import java.util.Optional;

public interface IOrganiserService extends Iservice<Organiser, Long> {
    Organiser save(Organiser organiser);
    Optional<Organiser> findById(Long id);
    List<Organiser> findAll();
    void deleteById(Long id);

    // ── Registration ──────────────────────────────────────────────────────
    Organiser registerOrganiser(Organiser organiser, Long facultyId);

    Event createEvent(Long organiserId, Event event);

    List<Event> findEventsByOrganiser(Long organiserId);

    Event updateEvent(Long organiserId,
                      Long eventId,
                      String title,
                      String description,
                      java.time.LocalDateTime eventDate,
                      Integer capacity,
                      za.ac.cput.campus_events.domain.Venue venue);

    void closeEvent(Long organiserId, Long eventId);

    void updateOrganiserStatus(Long organiserId, boolean active, Long requestingAdminId);
}
