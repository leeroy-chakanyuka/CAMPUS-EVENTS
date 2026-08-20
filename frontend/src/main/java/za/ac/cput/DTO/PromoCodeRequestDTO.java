/**
 * PromoCodeRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries promo code request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class PromoCodeRequestDTO {
    private String code;
    private String discountType;
    private Double value;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
