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
<<<<<<< HEAD
=======
    }

    /*
     * while this was not discussed in class, we noticed that we may need to override the value
     * of certain variables, but because the class is immutable, there's no setter to do so, a google
     * search informed us that the pattern to use in that case is the withX pattern as seen at the following 
     * thread : 
     */
    private Admin(Admin existing, String password) {
        this.id = existing.id;
        this.firstName = existing.firstName;
        this.lastName = existing.lastName;
        this.email = existing.email;
        this.createdAt = existing.createdAt;
        this.password = password;
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
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

<<<<<<< HEAD
        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
=======
        public Builder setPassword(String password){
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
            this.password = password;
            return this;
        }

<<<<<<< HEAD
        public Admin build() {
            return new Admin(this);
        }
=======
        public Builder setLastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email){
            this.email = email;
            return this;

        }

        public Admin build(){
            return new Admin(this);
        }
    }

    public Long getId() {
        return id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getPassword() {
        return password;
    }

    public Admin withPassword(String password) {
        return new Admin(this, password);
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
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
