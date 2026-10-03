package za.ac.cput.campus_events.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VenueResponseDTO {
    private Long id;
    private String name;
    private Integer capacity;
    private String city;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
