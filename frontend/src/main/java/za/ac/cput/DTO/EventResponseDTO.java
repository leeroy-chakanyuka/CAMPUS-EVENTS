/**
 * EventResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries event response data from backend to frontend.
 */
package za.ac.cput.campus_events.DTO;

public class EventResponseDTO {
    private Long id;
    private String title;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
