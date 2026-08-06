/**
 * StatusUpdateRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Reused across Student, Organiser, and Faculty status update requests.
 */
package src.main.java.za.ac.cput.DTO;

class
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
}

