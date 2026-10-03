package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.campus_events.domain.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Query for unread notifications by recipient
    List<Notification> findByRecipientIdAndRecipientTypeAndReadFalse(Long recipientId, String recipientType);
}
