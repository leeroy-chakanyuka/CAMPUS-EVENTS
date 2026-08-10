package za.ac.cput.DTO;

public class CreateAdminResponseDTO {
    private boolean success;
    private String message;
    private Long adminId;

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
}
