package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.Venue;
import java.util.List;
import java.util.Optional;

public interface IVenueService {
    // Basic CRUD
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void deleteById(Long id);

    // Custom queries
    List<Venue> findByName(String name);
    List<Venue> findByCapacityGreaterThan(int capacity);
    List<Venue> findByCity(String city);
}
