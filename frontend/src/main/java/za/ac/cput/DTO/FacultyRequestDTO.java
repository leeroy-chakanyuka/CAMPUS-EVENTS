/**
 * FacultyRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries faculty creation request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class FacultyRequestDTO {
    private String name;
    private String contactEmail;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
