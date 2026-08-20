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
    private String status;
    private String contactEmail;

    @ManyToOne
    @JoinColumn(name = "created_by_admin_id")
    private Admin createdByAdmin;

    private LocalDateTime createdAt = LocalDateTime.now();

    protected Faculty() {

    }

    private Faculty(Builder builder) {
        this.name = builder.name;
        this.status = builder.status;
        this.contactEmail = builder.contactEmail;
        this.createdByAdmin = builder.createdByAdmin;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
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

    public Faculty getFaculty() {
        return null;
    }

    public static class Builder {
        private String name;
        private String status;
        private String contactEmail;
        private Admin createdByAdmin;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        public Builder setStatus(String status) {
            this.status = status;
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
                ", status='"         + status           + '\'' +
                ", contactEmail='"   + contactEmail     + '\'' +
                ", createdByAdmin="  + createdByAdmin   +
                ", createdAt="       + createdAt         +
                '}';
    }
}