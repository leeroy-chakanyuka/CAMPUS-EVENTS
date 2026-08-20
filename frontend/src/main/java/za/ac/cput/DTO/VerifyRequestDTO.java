/**
 * VerifyRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries verification request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class VerifyRequestDTO {
    private String uuid;
    private String pin;

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
