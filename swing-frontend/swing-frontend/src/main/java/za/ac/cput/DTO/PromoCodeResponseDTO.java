/**
 * PromoCodeResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries promo code validation response data from backend to frontend.
 */
package za.ac.cput.DTO;

public class PromoCodeResponseDTO {
    private String code;
    private boolean valid;
    private String message;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
