package za.ac.cput.campus_events.domain;

import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String studentNumber; // some cool validation for this later
    private String password;

    private boolean isVerified = false; // we dont want to expose this to the builder, only the Admin

    private boolean active = true; // new students start active

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty; // link this with faculty class later

    protected Student(Builder builder) {
        this.faculty = builder.faculty;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.studentNumber = builder.studentNumber;
    }

    // immutable status change — same id, everything else copied as-is, only
    // `active` differs. Save the result and JPA updates the existing row.
    public Student(Student existing, boolean active) {
        this.id = existing.id;
        this.firstName = existing.firstName;
        this.lastName = existing.lastName;
        this.email = existing.email;
        this.studentNumber = existing.studentNumber;
        this.password = existing.password;
        this.isVerified = existing.isVerified;
        this.faculty = existing.faculty;
        this.active = active;
    }

    public Student() {
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

    public String getStudentNumber() {
        return studentNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public boolean isActive() {
        return active;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public static class Builder {

        private String firstName;
        private String lastName;
        private String email;
        private String studentNumber;
        private Faculty faculty;
        private String password;

        public Builder setPassword(String password){
            this.password = password;
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

        public Builder setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
            return this;
        }

        public Builder setFaculty(Faculty faculty) {
            this.faculty = faculty;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", isVerified=" + isVerified +
                ", active=" + active +
                ", faculty=" + faculty +
                '}';
    }
}
