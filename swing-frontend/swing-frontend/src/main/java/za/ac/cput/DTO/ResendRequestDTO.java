/**
 * ResendRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries resend request data from frontend to backend.
 */
package za.ac.cput.DTO;
<<<<<<< HEAD:frontend/src/main/java/za/ac/cput/DTO/ResendRequestDTO.java
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af:swing-frontend/swing-frontend/src/main/java/za/ac/cput/DTO/ResendRequestDTO.java

public class ResendRequestDTO {
    private String uuid;

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
}
