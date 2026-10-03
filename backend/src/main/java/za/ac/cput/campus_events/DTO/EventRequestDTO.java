/**
 * EventRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries event creation request data from frontend to backend.
 */
package za.ac.cput.campus_events.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequestDTO {
    private String title;
    private String description;
    private Long venueId;
    private String date;
    private String eventDate;
    private Integer capacity;
    private Long organiserId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Long getOrganiserId() { return organiserId; }
    public void setOrganiserId(Long organiserId) { this.organiserId = organiserId; }
}
