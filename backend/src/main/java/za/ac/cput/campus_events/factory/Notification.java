package za.ac.cput.campus_events.factory;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long recipientId;

    @Column(nullable = false)
    private String recipientType; // "STUDENT" or "ORGANISER"

    protected Notification() {}

    // Getters
    public Long getId() { return id; }
    public String getMessage() { return message; }
    public boolean isRead() { return read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getRecipientId() { return recipientId; }
    public String getRecipientType() { return recipientType; }

    // Setters
    public void setMessage(String message) { this.message = message; }
    public void setRead(boolean read) { this.read = read; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }

    // Helper
    public void markAsRead() { this.read = true; }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", read=" + read +
                ", createdAt=" + createdAt +
                ", recipientId=" + recipientId +
                ", recipientType='" + recipientType + '\'' +
                '}';
    }

    //  Builder implementation
    public static class Builder {
        private Long id;
        private String message;
        private boolean read;
        private LocalDateTime createdAt;
        private Long recipientId;
        private String recipientType;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder read(boolean read) {
            this.read = read;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder recipientId(Long recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public Builder recipientType(String recipientType) {
            this.recipientType = recipientType;
            return this;
        }

        public Builder copy(Notification notification) {
            this.id = notification.id;
            this.message = notification.message;
            this.read = notification.read;
            this.createdAt = notification.createdAt;
            this.recipientId = notification.recipientId;
            this.recipientType = notification.recipientType;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification();
            notification.id = this.id;
            notification.message = this.message;
            notification.read = this.read;
            notification.createdAt = this.createdAt;
            notification.recipientId = this.recipientId;
            notification.recipientType = this.recipientType;
            return notification;
        }
    }
}
