/**
 * StatusUpdateRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Reused across Student, Organiser, and Faculty status update requests.
 */
package za.ac.cput.campus_events.DTO;
/*
Mologadi Dikgale
Student Number: 231016263
 */

<<<<<<< HEAD
public class
StatusUpdateRequestDTO {
    private Long id;        // studentId, organiserId, or facultyId
    private boolean active; // true = activate/reactivate, false = suspend/deactivate
    private Long adminId;   // requesting admin

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
=======
public class StatusUpdateRequestDTO {
    private boolean active;
    private Long    requestingAdminId;

    public StatusUpdateRequestDTO() {}

    public StatusUpdateRequestDTO(boolean active, Long requestingAdminId) {
        this.active             = active;
        this.requestingAdminId  = requestingAdminId;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }
    public Long getRequestingAdminId()  {
        return requestingAdminId;
    }
    public void setRequestingAdminId(Long id) {
        this.requestingAdminId = id;
    }
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
}
