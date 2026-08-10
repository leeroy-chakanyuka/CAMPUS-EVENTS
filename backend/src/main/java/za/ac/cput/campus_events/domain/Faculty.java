package za.ac.cput.campus_events.domain;

import jakarta.persistence.*;
import za.ac.cput.campus_events.util.Helper;
import java.time.LocalDateTime;

/*
Mologadi Dikagle
student no:231016263
 */

@Entity
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactEmail;

    private boolean active = true; // new faculties start active

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id")
    private Admin createdByAdmin;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected Faculty() {

    }

    private Faculty(Builder builder) {
        this.name = builder.name;
        this.contactEmail = builder.contactEmail;
        this.createdByAdmin = builder.createdByAdmin;
    }

    public Faculty(Faculty existing, boolean active) {
        this.id = existing.id;
        this.name = existing.name;
        this.contactEmail = existing.contactEmail;
        this.createdByAdmin = existing.createdByAdmin;
        this.createdAt = existing.createdAt;
        this.active = active;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public Admin getCreatedByAdmin() {
        return createdByAdmin;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder {
        private String name;
        private String contactEmail;
        private Admin createdByAdmin;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setEmail(String contactEmail) {
            this.contactEmail = contactEmail;
            return this;
        }
        public Builder setCreatedByAdmin(Admin createdByAdmin) {
            this.createdByAdmin = createdByAdmin;
            return this;
        }

        public Faculty build() {
            return new Faculty(this);
        }
    }


    @Override
    public String toString() {
        return "Faculty{" +
                "id="                + id               +
                ", name='"           + name             + '\'' +
                ", active="          + active           +
                ", contactEmail='"   + contactEmail     + '\'' +
                ", createdByAdmin="  + createdByAdmin   +
                ", createdAt="       + createdAt         +
                '}';
    }
}
