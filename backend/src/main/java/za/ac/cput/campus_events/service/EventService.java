package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.repository.AdminRepository;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.service.IEventService;

import java.util.List;
import java.util.Optional;

@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final AdminRepository adminRepository;

    public EventService(EventRepository eventRepository, AdminRepository adminRepository) {
        this.eventRepository = eventRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event registerStudent(Long eventId) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventId);
//        if (optionalEvent.isPresent()) {
//            Event event = optionalEvent.get();
//            if (event.isOpen() && event.getCapacity() > 0) {
//                event.setCapacity(event.getCapacity() - 1);
//                if (event.getCapacity() == 0) {
//                    event.setOpen(false);
//                }
//                return eventRepository.save(event);
//            }
//        }
//        throw new IllegalStateException("Event not available for registration");
        return null;
    }

    @Override
    public Event cancelEvent(Long eventId) {
//        Optional<Event> optionalEvent = eventRepository.findById(eventId);
//        if (optionalEvent.isPresent()) {
//            Event event = optionalEvent.get();
//            event.setOpen(false);
//            return eventRepository.save(event);
//        }
//        throw new IllegalStateException("Event not found");
        return null;
    }

    @Override
    public void forceCancelEvent(Long id, Long adminId) {
        if (adminId == null || adminRepository.findById(adminId).isEmpty()) {
            throw new IllegalStateException("Admin only");
        }
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Event not found"));
        if (Boolean.FALSE.equals(existing.isOpen())) {
            throw new IllegalStateException("Event is already cancelled");
        }
        eventRepository.save(new Event(existing, false));
    }
}
