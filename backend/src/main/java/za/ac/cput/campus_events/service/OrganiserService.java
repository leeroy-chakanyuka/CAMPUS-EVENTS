package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.service.IOrganiserService;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.repository.FacultyRepository;
import za.ac.cput.campus_events.repository.OrganiserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrganiserService implements IOrganiserService {

    private final OrganiserRepository organiserRepository;
    private final FacultyRepository   facultyRepository;
    private final EventRepository     eventRepository;

    public OrganiserService(OrganiserRepository organiserRepository,
                            FacultyRepository facultyRepository,
                            EventRepository eventRepository) {
        this.organiserRepository = organiserRepository;
        this.facultyRepository   = facultyRepository;
        this.eventRepository     = eventRepository;
    }

    @Override
    public Organiser save(Organiser organiser) {
        return organiserRepository.save(organiser);
    }

    @Override
    public Optional<Organiser> findById(Long id) {
        return organiserRepository.findById(id);
    }

    @Override
    public List<Organiser> findAll() {
        return organiserRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        organiserRepository.deleteById(id);
    }

    // ── Register organiser — faculty must exist and be active ─────────────
    @Override
    public Organiser registerOrganiser(Organiser organiser, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException(
                        "Faculty not found: " + facultyId));

        if (!faculty.isActive()) {
            throw new RuntimeException(
                    "Cannot register organiser — faculty is not active");
        }

        return organiserRepository.save(organiser);
    }

    // ── Create event — gate is now isActive() not verificationStatus ──────
    @Override
    public Event createEvent(Long organiserId, Event event) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException(
                        "Organiser not found: " + organiserId));

        if (!organiser.isActive()) {
            throw new RuntimeException(
                    "Cannot create event — organiser is suspended");
        }

        Faculty faculty = facultyRepository.findById(organiser.getFacultyId())
                .orElseThrow(() -> new RuntimeException(
                        "Faculty not found for organiser: " + organiserId));

        if (!faculty.isActive()) {
            throw new RuntimeException(
                    "Cannot create event — faculty is not active");
        }

        return eventRepository.save(event);
    }

    // ── Update event — same gate ───────────────────────────────────────────
    @Override
    public Event updateEvent(Long organiserId, Event event) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException(
                        "Organiser not found: " + organiserId));

        if (!organiser.isActive()) {
            throw new RuntimeException(
                    "Cannot update event — organiser is suspended");
        }

        Faculty faculty = facultyRepository.findById(organiser.getFacultyId())
                .orElseThrow(() -> new RuntimeException(
                        "Faculty not found for organiser: " + organiserId));

        if (!faculty.isActive()) {
            throw new RuntimeException(
                    "Cannot update event — faculty is not active");
        }

        return eventRepository.save(event);
    }

    // ── Close event — same gate ───────────────────────────────────────────
    @Override
    public void closeEvent(Long organiserId, Long eventId) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException(
                        "Organiser not found: " + organiserId));

        if (!organiser.isActive()) {
            throw new RuntimeException(
                    "Cannot close event — organiser is suspended");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException(
                        "Event not found: " + eventId));

        event.closeRegistration();
        eventRepository.save(event);
    }

    // ── Status update — immutable copy constructor pattern ────────────────
    @Override
    public void updateOrganiserStatus(Long organiserId, boolean active,
                                      Long requestingAdminId) {
        if (requestingAdminId == null) {
            throw new IllegalStateException("Admin only");
        }

        Organiser existing = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException(
                        "Organiser not found: " + organiserId));

        Organiser updated = new Organiser(existing, active);
        organiserRepository.save(updated);
    }
}
