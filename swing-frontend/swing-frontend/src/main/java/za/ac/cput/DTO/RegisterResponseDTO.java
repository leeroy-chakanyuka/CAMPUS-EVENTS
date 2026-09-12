/**
 * RegisterResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Returns registration response data from backend to frontend.
 */
package za.ac.cput.DTO;
<<<<<<< HEAD:frontend/src/main/java/za/ac/cput/DTO/RegisterResponseDTO.java
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af:swing-frontend/swing-frontend/src/main/java/za/ac/cput/DTO/RegisterResponseDTO.java

public class RegisterResponseDTO {
    private boolean success;
    private String message;
    private String uuid;
    private String email;
    private String pin; // DEV MODE ONLY: temporary stand-in for email delivery

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
