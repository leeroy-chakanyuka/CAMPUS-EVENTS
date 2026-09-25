package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Notification;

import static org.junit.jupiter.api.Assertions.*;

class NotificationFactoryTest {

    @Test
    void createNotificationSuccessfully() {

        Notification notification = NotificationFactory.createNotification(
                "You have successfully registered.",
                1L,
                "STUDENT"
        );

        assertNotNull(notification);
        assertEquals("You have successfully registered.", notification.getMessage());
        assertEquals(1L, notification.getRecipientId());
        assertEquals("STUDENT", notification.getRecipientType());
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void shouldReturnNullWhenMessageIsNull() {

        Notification notification = NotificationFactory.createNotification(
                null,
                1L,
                "STUDENT"
        );

        assertNull(notification);
    }

    @Test
    void shouldReturnNullWhenMessageIsBlank() {

        Notification notification = NotificationFactory.createNotification(
                "",
                1L,
                "STUDENT"
        );

        assertNull(notification);
    }

    @Test
    void shouldReturnNullWhenRecipientIdIsNull() {

        Notification notification = NotificationFactory.createNotification(
                "You have successfully registered.",
                null,
                "STUDENT"
        );

        assertNull(notification);
    }

    @Test
    void shouldReturnNullWhenRecipientTypeIsNull() {

        Notification notification = NotificationFactory.createNotification(
                "You have successfully registered.",
                1L,
                null
        );

        assertNull(notification);
    }

    @Test
    void shouldReturnNullWhenRecipientTypeIsBlank() {

        Notification notification = NotificationFactory.createNotification(
                "You have successfully registered.",
                1L,
                ""
        );

        assertNull(notification);
    }
}
