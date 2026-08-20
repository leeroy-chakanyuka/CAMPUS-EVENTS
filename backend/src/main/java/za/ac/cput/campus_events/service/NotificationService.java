package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.repository.NotificationRepository;
import za.ac.cput.campus_events.service.INotificationService;

import java.time.LocalDateTime;

@Service
public class NotificationService implements INotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification sendNotification(String message, Long recipientId, String recipientType) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipientId(recipientId);
        notification.setRecipientType(recipientType);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    @Override
    public void markAsRead(Long notificationId) {
        repository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            repository.save(n);
        });
    }
}
