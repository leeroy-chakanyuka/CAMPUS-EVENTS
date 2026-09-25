/**
 * VerifyRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries verification request data from frontend to backend.
 */
<<<<<<< HEAD:frontend/src/main/java/za/ac/cput/DTO/VerifyRequestDTO.java
package  za.ac.cput.DTO;
=======
package za.ac.cput.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af:swing-frontend/swing-frontend/src/main/java/za/ac/cput/DTO/VerifyRequestDTO.java

public class VerifyRequestDTO {
    private String uuid;
    private String pin;

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
