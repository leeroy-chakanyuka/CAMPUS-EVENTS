package za.ac.cput.campus_events.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PendingRegistration {

    @Id
    private String uuid;

    private String email;
    private String password;
    private String role;
    private Long facultyId;

    @Nullable
    private String studentNumber;

    private String pin;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    protected PendingRegistration() {
    }

    private PendingRegistration(Builder builder) {
        this.uuid = builder.uuid;
        this.email = builder.email;
        this.password = builder.password;
        this.role = builder.role;
        this.facultyId = builder.facultyId;
        this.studentNumber = builder.studentNumber;
        this.pin = builder.pin;
        this.expiresAt = builder.expiresAt;
        this.createdAt = builder.createdAt;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPin() {
        return pin;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public static class Builder {

        private String uuid = UUID.randomUUID().toString();
        private String email;
        private String password;
        private String role;
        private Long facultyId;
        private String studentNumber;
        private String pin;
        private LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setFacultyId(Long facultyId) {
            this.facultyId = facultyId;
            return this;
        }

        public Builder setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder setPin(String pin) {
            this.pin = pin;
            return this;
        }

        public Builder setExpiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PendingRegistration build() {
            return new PendingRegistration(this);
        }
    }

    @Override
    public String toString() {
        return "PendingRegistration{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", facultyId=" + facultyId +
                ", studentNumber='" + studentNumber + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}