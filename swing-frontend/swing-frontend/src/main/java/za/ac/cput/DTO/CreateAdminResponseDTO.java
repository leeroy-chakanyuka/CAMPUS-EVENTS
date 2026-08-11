package za.ac.cput.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CreateAdminResponseDTO {
    private boolean success;
    private String message;
    private Long adminId;
    private String uuid;

    public CreateAdminResponseDTO() {}

    public CreateAdminResponseDTO(boolean success, String message, Long adminId) {
        this.success = success;
        this.message = message;
        this.adminId = adminId;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
}
