package za.ac.cput.campus_events.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt = new Date();
    private String password;

    public Admin(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
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
    }

    public Admin(){}

    public static class Builder{
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public Builder setFirstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public Builder setPassword(String password){
            this.password = password;
            return this;
        }

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
