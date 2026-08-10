package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.service.IEventService;

import java.util.Optional;

@Service
public class EventService implements IEventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}
