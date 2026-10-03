package za.ac.cput.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private Long accountId;
    private String role;
    private Boolean readOnly;

    public LoginResponseDTO() {}

    public LoginResponseDTO(boolean success, String message, Long accountId, String role) {
        this.success = success;
        this.message = message;
        this.accountId = accountId;
        this.role = role;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getReadOnly() { return readOnly; }
    public void setReadOnly(Boolean readOnly) { this.readOnly = readOnly; }
}
