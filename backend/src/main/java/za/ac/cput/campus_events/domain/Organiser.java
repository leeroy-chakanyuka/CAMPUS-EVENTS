package za.ac.cput.campus_events.domain;
/*
Dikgale Mologadi
Student No: 231016263
 */

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Organiser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    private boolean active = true; // new organisers start active

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToMany(mappedBy = "organiser", fetch = FetchType.LAZY)
    private List<Event> events;

    protected Organiser() {}

    private Organiser(Builder builder) {
        this.id        = builder.id;
        this.firstName = builder.firstName;
        this.lastName  = builder.lastName;
        this.email     = builder.email;
        this.role      = builder.role;
        this.createdAt = builder.createdAt;
        this.faculty   = builder.faculty;
        // we'll create an addEvent method so for now just init
        this.events = new ArrayList<>();
    }

    // immutable status change — same id, everything else copied as-is, only
    // `active` differs. Save the result and JPA updates the existing row.
    public Organiser(Organiser existing, boolean active) {
        this.id        = existing.id;
        this.firstName = existing.firstName;
        this.lastName  = existing.lastName;
        this.email     = existing.email;
        this.role      = existing.role;
        this.createdAt = existing.createdAt;
        this.faculty   = existing.faculty;
        this.events    = existing.events;
        this.active    = active;
    }

    public Long getId(){
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName()  {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public List<Event> getEvents() {
        return events;
    }
    public boolean isActive(){
        return active;
    }
    public Faculty getFaculty(){
        return faculty;
    }

    @Override
    public String toString() {
        return "Organiser{" +
                "id="           + id          +
                ", firstName='" + firstName   + '\'' +
                ", lastName='"  + lastName    + '\'' +
                ", email='"     + email       + '\'' +
                ", role='"      + role        + '\'' +
                ", active="     + active      +
                ", faculty="    + faculty     +
                ", createdAt="  + createdAt   +
                '}';
    }

    public static class Builder {
        private Long          id;
        private String        firstName;
        private String        lastName;
        private String        email;
        private String        role;
        private LocalDateTime createdAt;
        private Faculty        faculty;
        private List<Event>   events;


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
        public Builder setRole(String role) {
            this.role = role;
            return this;
        }
        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder setFaculty(Faculty faculty) {
            this.faculty = faculty;
            return this;
        }

        public Organiser build() {

            return new Organiser(this);
        }
    }

}
