package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Venue;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByName(String name);
    List<Venue> findByCapacityGreaterThan(int capacity);
    List<Venue> findByAddress_City(String city); // query using embedded Address field
}
