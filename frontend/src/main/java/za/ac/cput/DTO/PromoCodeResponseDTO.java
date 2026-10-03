/**
 * PromoCodeResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries promo code validation and management data from backend to frontend.
 */
package za.ac.cput.campus_events.DTO;

import java.time.LocalDate;

public class PromoCodeResponseDTO {
    private String id;
    private String code;
    private String discountType;
    private double value;
    private String scopeType;
    private boolean active;
    private int timesUsed;
    private int maxRedemptions;
    private LocalDate startDate;
    private LocalDate expiryDate;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getScopeType() { return scopeType; }
    public void setScopeType(String scopeType) { this.scopeType = scopeType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getTimesUsed() { return timesUsed; }
    public void setTimesUsed(int timesUsed) { this.timesUsed = timesUsed; }

    public int getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(int maxRedemptions) { this.maxRedemptions = maxRedemptions; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
