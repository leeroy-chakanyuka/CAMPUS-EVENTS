package za.ac.cput.campus_events.DTO;
/*
Mologadi Dikgale
Student Number: 231016263
 */

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
}
