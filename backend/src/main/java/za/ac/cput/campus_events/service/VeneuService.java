package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.repository.VenueRepository;
import za.ac.cput.campus_events.service.IVenueService;

import java.util.List;
import java.util.Optional;

@Service
public class VeneuService implements IVenueService {

    private final VenueRepository venueRepository;
    
    public VeneuService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        venueRepository.deleteById(id);
    }

    @Override
    public List<Venue> findByName(String name) {
        return venueRepository.findByName(name);
    }

    @Override
    public List<Venue> findByCapacityGreaterThan(int capacity) {
        return venueRepository.findByCapacityGreaterThan(capacity);
    }

    @Override
    public List<Venue> findByCity(String city) {
        return venueRepository.findByAddress_City(city);
    }
}
