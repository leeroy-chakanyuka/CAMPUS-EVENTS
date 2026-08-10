package za.ac.cput.campus_events.DTO;
/*
Mologadi Dikgale
Student Number: 231016263
 */

public class FacultyRequestDTO {
    private String name;
    private String contactEmail;
    private Long   adminId;

    public FacultyRequestDTO() {}

    public FacultyRequestDTO(String name, String contactEmail, Long adminId) {
        this.name         = name;
        this.contactEmail = contactEmail;
        this.adminId      = adminId;
    }

    public String getName()  {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public void setContactEmail(String email){
        this.contactEmail = email;
    }
    public Long getAdminId() {
        return adminId;
    }
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
