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

    private String firstName;
    private String lastName;
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
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public PendingRegistration withPin(String pin) {
        return toBuilder().setPin(pin).build();
    }

    public PendingRegistration withExpiresAt(LocalDateTime expiresAt) {
        return toBuilder().setExpiresAt(expiresAt).build();
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    private Builder toBuilder() {
        return new Builder()
                .setUuid(uuid)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setRole(role)
                .setFacultyId(facultyId)
                .setStudentNumber(studentNumber)
                .setPin(pin)
                .setExpiresAt(expiresAt)
                .setCreatedAt(createdAt);
    }

    public static class Builder {

        private String uuid = UUID.randomUUID().toString();
        private String firstName;
        private String lastName;
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

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
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
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", facultyId=" + facultyId +
                ", studentNumber='" + studentNumber + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}