package za.ac.cput.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusUpdateRequestDTO {
    private boolean active;
    private Long requestingAdminId;

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getRequestingAdminId() { return requestingAdminId; }
    public void setRequestingAdminId(Long requestingAdminId) { this.requestingAdminId = requestingAdminId; }
}
