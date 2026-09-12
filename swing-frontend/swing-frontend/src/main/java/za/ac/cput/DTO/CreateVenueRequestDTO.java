/**
 * CreateVenueRequestDTO
 * Author: Faith Adams (Student #222297204)
 * Purpose: Carries venue creation request data from frontend to backend.
 */
package za.ac.cput.DTO;

public class CreateVenueRequestDTO {
    private String name;
    private Integer capacity;
    private String address;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
