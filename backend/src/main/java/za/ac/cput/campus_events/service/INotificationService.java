package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.Notification;

import java.util.List;

public interface INotificationService {
    Notification sendNotification(String message, Long recipientId, String recipientType);
    List<Notification> notificationsFor(Long recipientId, String recipientType);
    void markAsRead(Long notificationId);
}
