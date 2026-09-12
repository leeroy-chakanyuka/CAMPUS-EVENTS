package za.ac.cput.campus_events.DTO;

import java.time.LocalDateTime;

public class TicketResponseDTO {
    private Long id;
    private Double price;
    private String status;
    private Long studentId;
    private Long eventId;
    private Long promoCodeId;
    private LocalDateTime createdAt; // stamped automatically, no setter

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getPromoCodeId() { return promoCodeId; }
    public void setPromoCodeId(Long promoCodeId) { this.promoCodeId = promoCodeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    //  No setter for createdAt — stamped automatically at construction
}
