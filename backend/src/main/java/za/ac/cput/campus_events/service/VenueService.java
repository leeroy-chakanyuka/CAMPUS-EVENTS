package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Venue;
import za.ac.cput.campus_events.repository.VenueRepository;

import java.util.List;

@Service
public class VenueService implements IVenueService {
    private final VenueRepository repository;

    public VenueService(VenueRepository repository) {
        this.repository = repository;
    }

    @Override
    public Venue create(Venue venue) {
        return repository.save(venue);
    }

    @Override
    public Venue update(Long id, Venue venue) {
        Venue existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        existing.setName(venue.getName());
        existing.setCapacity(venue.getCapacity());
        existing.setAddress(venue.getAddress());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Venue findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Venue> findAll() {
        return repository.findAll();
    }
}
