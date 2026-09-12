package za.ac.cput.campus_events.DTO;

import java.time.LocalDateTime;

public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private Integer capacity;
    private boolean open;
    private String venueName;
    private String organiserName;
    private String facultyName;
    private LocalDateTime createdAt; // stamped automatically, no setter

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getOrganiserName() { return organiserName; }
    public void setOrganiserName(String organiserName) { this.organiserName = organiserName; }

    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    //  No setter for createdAt — stamped automatically at construction
}
