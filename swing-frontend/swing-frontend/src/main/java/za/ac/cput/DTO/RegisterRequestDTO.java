/**
 * RegisterRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries registration request data from frontend to backend.
 */
package za.ac.cput.DTO;
<<<<<<< HEAD:frontend/src/main/java/za/ac/cput/DTO/RegisterRequestDTO.java
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af:swing-frontend/swing-frontend/src/main/java/za/ac/cput/DTO/RegisterRequestDTO.java

public class RegisterRequestDTO {
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Long facultyId;
    private String studentNumber; // nullable

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
}
