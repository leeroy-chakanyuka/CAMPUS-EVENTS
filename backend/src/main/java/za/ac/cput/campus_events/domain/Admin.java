/**
 * Admin
 * Author: Faith Adams (Student #222297204)
 * Purpose: Represents an Admin account in the system.
 */
package za.ac.cput.campus_events.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private Date createdAt = new Date();
    private String password;

    // Default constructor for JPA
    public Admin() {}

    // Builder-based constructor
    private Admin(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getCreatedAt() { return createdAt; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Builder Pattern
    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;

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

        public Admin build() {
            return new Admin(this);
        }
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
