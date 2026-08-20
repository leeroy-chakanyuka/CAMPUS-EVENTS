/**
 * RegisterRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries registration request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class RegisterRequestDTO {
    private String role;
    private String email;
    private String password;
    private Long facultyId;
    private String studentNumber; // nullable

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
}
