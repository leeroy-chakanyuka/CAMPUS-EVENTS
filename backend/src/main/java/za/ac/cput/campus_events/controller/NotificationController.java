package za.ac.cput.campus_events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.NotificationResponseDTO;
import za.ac.cput.campus_events.DTO.SendNotificationRequestDTO;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.service.INotificationService;

import java.util.List;

/**
 * Notification inbox. GET takes the recipient type as a path variable
 * (e.g. /notification/organiser/1 or /notification/ORGANISER/1 — matched
 * case-insensitively), which is what all three Swing panels call.
 * Nothing here sends mail or fires on its own — notifications are created
 * server-side when things happen (event created/closed, status changes).
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final INotificationService service;

    public NotificationController(INotificationService service) {
        this.service = service;
    }

    @GetMapping("/{recipientType}/{recipientId}")
    public ResponseEntity<List<NotificationResponseDTO>> inbox(@PathVariable String recipientType,
                                                               @PathVariable Long recipientId) {
        return ResponseEntity.ok(service.notificationsFor(recipientId, recipientType)
                .stream()
                .map(this::toResponse)
                .toList());
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendNotificationRequestDTO request) {
        try {
            if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
                return ResponseEntity.badRequest().body("Message is required");
            }
            if (request.getRecipientId() == null || request.getRecipientType() == null
                    || request.getRecipientType().isBlank()) {
                return ResponseEntity.badRequest().body("Recipient is required");
            }
            return ResponseEntity.ok(toResponse(service.sendNotification(
                    request.getMessage(), request.getRecipientId(), request.getRecipientType())));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    private NotificationResponseDTO toResponse(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setRecipientId(notification.getRecipientId());
        dto.setRecipientType(notification.getRecipientType());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
