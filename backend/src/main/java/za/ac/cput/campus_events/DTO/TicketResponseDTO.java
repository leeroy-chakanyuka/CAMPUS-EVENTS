/**
 * TicketResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries ticket response data from backend to frontend.
 */
package za.ac.cput.campus_events.DTO;

public class TicketResponseDTO {
    private Long id;
    private String status;
    private Double price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
