package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.repository.FacultyRepository;
import za.ac.cput.campus_events.repository.OrganiserRepository;
import za.ac.cput.campus_events.repository.VenueRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrganiserService implements IOrganiserService {
    // TODO : COME BACK AND SEE IF WE CAN USE OBJECTS AND DTOS INSTEAD OF ALL THESE MULTIPLE PARAMS
    private final OrganiserRepository organiserRepository;
    private final FacultyRepository facultyRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public OrganiserService(OrganiserRepository organiserRepository,
                            FacultyRepository facultyRepository,
                            EventRepository eventRepository,
                            VenueRepository venueRepository) {
        this.organiserRepository = organiserRepository;
        this.facultyRepository = facultyRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public Organiser create(Organiser organiser) { return organiserRepository.save(organiser); }

    @Override
    public Organiser read(Long id) {
        return organiserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organiser not found: " + id));
    }

    @Override
    public List<Organiser> findAll() { return organiserRepository.findAll(); }

    @Override
    public Organiser update(Organiser organiser) { return organiserRepository.save(organiser); }

    @Override
    public void delete(Long id) {
        if (!eventRepository.findByOrganiserId(id).isEmpty()) {
            throw new RuntimeException("Cannot delete organiser with events — suspend instead");
        }
        organiserRepository.deleteById(id);
    }

    @Override
    public Organiser registerOrganiser(Organiser organiser, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found: " + facultyId));
        if (!faculty.isActive()) {
            throw new RuntimeException("Cannot register organiser — faculty is not active");
        }
        return organiserRepository.save(organiser);
    }

    private Organiser requireActiveOrganiser(Long organiserId) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException("Organiser not found: " + organiserId));
        if (!organiser.isActive()) {
            throw new RuntimeException("Cannot manage event — organiser is suspended");
        }
        if (organiser.getFaculty() == null) {
            throw new RuntimeException("Faculty not found for organiser: " + organiserId);
        }
        if (!organiser.getFaculty().isActive()) {
            throw new RuntimeException("Cannot manage event — faculty is not active");
        }
        return organiser;
    }

    private Venue requireVenue(Long venueId) {
        return venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found: " + venueId));
    }

    private void validateEventDetails(String title, LocalDateTime eventDate,
                                      Integer capacity, Venue venue) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("Title is required");
        }
        if (eventDate == null) {
            throw new RuntimeException("Date is required");
        }
        if (capacity == null || capacity <= 0) {
            throw new RuntimeException("Capacity must be greater than zero");
        }
        if (venue.getCapacity() != null && capacity > venue.getCapacity()) {
            throw new RuntimeException("Capacity exceeds venue capacity (" + venue.getCapacity() + ")");
        }
    }

    private void validateNewEvent(String title, LocalDateTime eventDate,
                                  Integer capacity, Venue venue) {
        validateEventDetails(title, eventDate, capacity, venue);
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Event date must be in the future");
        }
    }

    @Override
    public Event createEvent(Long organiserId, String title, String description,
                             LocalDateTime eventDate, Integer capacity, Long venueId) {
        Organiser organiser = requireActiveOrganiser(organiserId);
        Venue venue = requireVenue(venueId);
        validateNewEvent(title, eventDate, capacity, venue);

        Event event = new Event.Builder()
                .setTitle(title)
                .setDescription(description)
                .setEventDate(eventDate)
                .setCapacity(capacity)
                .setOpen(true)
                .setCreatedAt(LocalDateTime.now())
                .setVenue(venue)
                .setOrganiser(organiser)
                .setFaculty(organiser.getFaculty())
                .build();

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long organiserId, Long eventId, String title, String description,
                             LocalDateTime eventDate, Integer capacity, Long venueId) {
        Organiser organiser = requireActiveOrganiser(organiserId);
        Venue venue = requireVenue(venueId);
        Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        if (existing.getOrganiser() == null || !organiserId.equals(existing.getOrganiser().getId())) {
            throw new RuntimeException("You can only update your own events");
        }
        if (Boolean.FALSE.equals(existing.isOpen())) {
            throw new RuntimeException("Cannot edit a closed event");
        }
        validateEventDetails(title, eventDate, capacity, venue);

        Event updated = new Event(existing, title, description, eventDate, capacity, venue);
        return eventRepository.save(updated);
    }

    @Override
    public void closeEvent(Long organiserId, Long eventId) {
        Organiser organiser = requireActiveOrganiser(organiserId);
        Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        if (existing.getOrganiser() == null || !organiserId.equals(existing.getOrganiser().getId())) {
            throw new RuntimeException("You can only close your own events");
        }

        eventRepository.save(new Event(existing, false));
    }

    @Override
    public List<Event> findEventsByOrganiser(Long organiserId) {
        return eventRepository.findByOrganiserId(organiserId);
    }

    @Override
    public void updateOrganiserStatus(Long organiserId, boolean active, Long requestingAdminId) {
        if (requestingAdminId == null) throw new IllegalStateException("Admin only");
        Organiser existing = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException("Organiser not found: " + organiserId));
        organiserRepository.save(new Organiser(existing, active));
    }
}
