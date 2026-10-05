package za.ac.cput.campus_events.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.EventRequestDTO;
import za.ac.cput.campus_events.DTO.EventResponseDTO;
import za.ac.cput.campus_events.DTO.StatusUpdateRequestDTO;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.service.IEventService;
import za.ac.cput.campus_events.service.IOrganiserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Exposes the exact endpoints the organiser Swing client already calls:
 * POST /event, PUT /event/{id}, PUT /event/{id}/close, GET /event/organiser/{organiserId}.
 * All business rules (active organiser/faculty, ownership, venue lookup) live in
 * OrganiserService — this layer only maps HTTP to service calls.
 */
@RestController
@RequestMapping("/event")
public class EventController {

    private final IOrganiserService organiserService;
    private final IEventService eventService;

    public EventController(IOrganiserService organiserService, IEventService eventService) {
        this.organiserService = organiserService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> allEvents() {
        return ResponseEntity.ok(eventService.findAll().stream().map(this::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventRequestDTO dto) {
        try {
            validate(dto);
            Event saved = organiserService.createEvent(
                    dto.getOrganiserId(),
                    dto.getTitle(),
                    dto.getDescription(),
                    parseDate(rawDate(dto)),
                    dto.getCapacity(),
                    dto.getVenueId());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,
                                         @RequestBody EventRequestDTO dto) {
        try {
            validate(dto);
            Event saved = organiserService.updateEvent(
                    dto.getOrganiserId(),
                    id,
                    dto.getTitle(),
                    dto.getDescription(),
                    parseDate(rawDate(dto)),
                    dto.getCapacity(),
                    dto.getVenueId());
            return ResponseEntity.ok(toResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<?> closeEvent(@PathVariable Long id,
                                        @RequestParam Long organiserId) {
        try {
            organiserService.closeEvent(organiserId, id);
            return ResponseEntity.ok("Registration closed");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/force-cancel")
    public ResponseEntity<?> forceCancel(@PathVariable Long id,
                                        @RequestBody StatusUpdateRequestDTO dto) {
        try {
            eventService.forceCancelEvent(id, dto.getRequestingAdminId());
            return ResponseEntity.ok("Event cancelled");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/organiser/{organiserId}")
    public ResponseEntity<?> eventsByOrganiser(@PathVariable Long organiserId) {
        try {
            List<EventResponseDTO> events = organiserService.findEventsByOrganiser(organiserId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(events);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void validate(EventRequestDTO dto) {
        if (dto == null) throw new RuntimeException("Event details are required");
        if (dto.getTitle() == null || dto.getTitle().isBlank()) throw new RuntimeException("Title is required");
        if (rawDate(dto) == null || rawDate(dto).isBlank()) throw new RuntimeException("Date is required");
        if (dto.getCapacity() == null) throw new RuntimeException("Capacity is required");
        if (dto.getVenueId() == null) throw new RuntimeException("Venue is required");
        if (dto.getOrganiserId() == null) throw new RuntimeException("Organiser is required");
    }

    private String rawDate(EventRequestDTO dto) {
        return dto.getEventDate() != null ? dto.getEventDate() : dto.getDate();
    }

    private LocalDateTime parseDate(String raw) {
        try {
            return LocalDateTime.parse(raw);
        } catch (Exception first) {
            try {
                return LocalDate.parse(raw).atStartOfDay();
            } catch (Exception second) {
                throw new RuntimeException("Invalid date, expected yyyy-MM-dd or yyyy-MM-ddTHH:mm:ss: " + raw);
            }
        }
    }

    private EventResponseDTO toResponse(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate() == null ? null : event.getEventDate().toString());
        dto.setCapacity(event.getCapacity());
        dto.setOpen(event.isOpen());
        dto.setTicketsSold(event.getTickets() == null ? 0 : event.getTickets().size());
        if (event.getVenue() != null) {
            dto.setVenueId(event.getVenue().getId());
            dto.setVenueName(event.getVenue().getName());
        }
        if (event.getFaculty() != null) {
            dto.setFacultyName(event.getFaculty().getName());
        }
        if (event.getOrganiser() != null) {
            dto.setOrganiserName(event.getOrganiser().getFirstName() + " " + event.getOrganiser().getLastName());
        }
        return dto;
    }
}
