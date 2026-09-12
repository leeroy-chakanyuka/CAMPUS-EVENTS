package za.ac.cput.campus_events.DTO;

import java.time.LocalDate;

public class PromoCodeRequestDTO {
    private String code;
    private String discountType;
    private Double value;
    private String scopeType;   // decides whether eventId or facultyId applies
    private Long eventId;       // nullable
    private Long facultyId;     // nullable
    private Integer maxRedemptions;
    private LocalDate startDate;
    private LocalDate expiryDate;

    // Getters and setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getScopeType() { return scopeType; }
    public void setScopeType(String scopeType) { this.scopeType = scopeType; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }

    public Integer getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(Integer maxRedemptions) { this.maxRedemptions = maxRedemptions; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
