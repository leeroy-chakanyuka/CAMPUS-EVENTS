/**
 * EventResponseDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries event response data from backend to frontend.
 */
package za.ac.cput.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String eventDate;
    private Integer capacity;
    private Boolean open;
    private Long venueId;
    private String venueName;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getOpen() { return open; }
    public void setOpen(Boolean open) { this.open = open; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
