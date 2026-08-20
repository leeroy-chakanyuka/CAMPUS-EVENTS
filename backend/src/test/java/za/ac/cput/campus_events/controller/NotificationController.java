package za.ac.cput.campus_events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.DTO.SendNotificationRequestDTO;
import za.ac.cput.campus_events.service.INotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final INotificationService service;

    public NotificationController(INotificationService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> send(@RequestBody SendNotificationRequestDTO request) {
        Notification notification = service.sendNotification(
                request.getMessage(),
                request.getRecipientId(),
                request.getRecipientType()
        );
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
