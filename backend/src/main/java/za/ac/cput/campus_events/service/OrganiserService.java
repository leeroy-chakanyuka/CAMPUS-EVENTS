package za.ac.cput.campus_events.service;
/*
Mologadi Dikgale
Student No: 231016263
 */

import org.springframework.stereotype.Service;
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
    private final FacultyRepository facultyRepository;
    private final EventRepository eventRepository;

    public OrganiserService(OrganiserRepository organiserRepository,
                            FacultyRepository facultyRepository,
                            EventRepository eventRepository) {
        this.organiserRepository = organiserRepository;
        this.facultyRepository = facultyRepository;
        this.eventRepository = eventRepository;
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

    @Override
    public Organiser registerOrganiser(Organiser organiser, Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId)).getFaculty();

        if (!faculty.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException("Cannot register organiser — faculty is not ACTIVE");
        }

        organiser.setFaculty(faculty); // assuming Organiser has a Faculty field
        return organiserRepository.save(organiser);
    }

    @Override
    public Event createEvent(Long organiserId, Event event) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException("Organiser not found with id: " + organiserId));

        Faculty faculty = organiser.getFaculty(); // FIXED: use organiser’s faculty
        if (faculty == null || !faculty.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException("Cannot create event — faculty is not ACTIVE");
        }

        event.setOrganiser(organiser); // assuming Event has an organiser field
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long organiserId, Event event) {
        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException("Organiser not found with id: " + organiserId));

        Faculty faculty = organiser.getFaculty(); // FIXED
        if (faculty == null || !faculty.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException("Cannot update event — faculty is not ACTIVE");
        }

        event.setOrganiser(organiser);
        return eventRepository.save(event);
    }

    @Override
    public void closeEvent(Long organiserId, Long eventId) {

    }

    public boolean existsByEmail(String email) {
    }
}
