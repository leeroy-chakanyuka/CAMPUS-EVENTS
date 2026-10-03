/**
 * EventRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries event creation request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class EventRequestDTO {
    private String title;
    private String description;
    private Long venueId;
    private String date;
    private Integer capacity;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
