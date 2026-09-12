/**
 * VerifyResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Returns verification response data from backend to frontend.
 */
package za.ac.cput.DTO;
<<<<<<< HEAD:frontend/src/main/java/za/ac/cput/DTO/VerifyResponseDTO.java
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af:swing-frontend/swing-frontend/src/main/java/za/ac/cput/DTO/VerifyResponseDTO.java

public class VerifyResponseDTO {
    private boolean success;
    private String message;
    private Long accountId;
    private String role;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
