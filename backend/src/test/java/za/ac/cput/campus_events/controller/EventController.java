package za.ac.cput.campus_events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.service.IEventService;

@RestController
@RequestMapping("/event")
public class EventController {

    private final IEventService eventService;

    public EventController(IEventService eventService) {
        this.eventService = eventService;
    }

    @PutMapping("/{id}/force-cancel")
    public ResponseEntity<String> forceCancelEvent(@PathVariable Long id,
                                                   @RequestParam Long adminId) {
        eventService.forceCancelEvent(id, adminId);
        return ResponseEntity.ok("Event cancelled successfully");
    }
}
