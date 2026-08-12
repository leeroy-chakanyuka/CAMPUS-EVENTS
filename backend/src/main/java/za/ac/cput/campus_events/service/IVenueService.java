package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.Venue;
import java.util.List;

public interface IVenueService {
    Venue create(Venue venue);
    Venue update(Long id, Venue venue);
    void delete(Long id);
    Venue findById(Long id);
    List<Venue> findAll();
}
