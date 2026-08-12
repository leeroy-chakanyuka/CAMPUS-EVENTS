package za.ac.cput.campus_events.factory;

import za.ac.cput.campus_events.domain.Notification;
import java.time.LocalDateTime;

public class NotificationFactory {

    public static Notification createNotification(String message,
                                                  Long recipientId,
                                                  String recipientType) {

        if (message == null || message.isBlank())
            return null;

        if (recipientId == null)
            return null;

        if (recipientType == null || recipientType.isBlank())
            return null;

        return new Notification.Builder()
                .setMessage(message)
                .setRecipientId(recipientId)
                .setRecipientType(recipientType)
                .setRead(false)
                .setCreatedAt(LocalDateTime.now())
                .build();
    }
}
