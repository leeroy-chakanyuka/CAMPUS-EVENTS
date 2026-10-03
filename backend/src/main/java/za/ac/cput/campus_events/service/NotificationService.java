package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.repository.NotificationRepository;
import za.ac.cput.campus_events.service.INotificationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService implements INotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification sendNotification(String message, Long recipientId, String recipientType) {
        Notification notification = new Notification();
        notification.setTitle(headline(message));
        notification.setMessage(message);
        notification.setRecipientId(recipientId);
        notification.setRecipientType(normaliseType(recipientType));
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    @Override
    public List<Notification> notificationsFor(Long recipientId, String recipientType) {
        return repository.findByRecipientIdAndRecipientTypeAndReadFalseOrderByCreatedAtDesc(
                recipientId, normaliseType(recipientType));
    }

    @Override
    public void markAsRead(Long notificationId) {
        repository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }

    private String normaliseType(String recipientType) {
        return recipientType == null ? null : recipientType.trim().toUpperCase();
    }

    private String headline(String message) {
        if (message == null) return "";
        String flat = message.replaceAll("\\s+", " ").trim();
        return flat.length() <= 200 ? flat : flat.substring(0, 200);
    }
}
