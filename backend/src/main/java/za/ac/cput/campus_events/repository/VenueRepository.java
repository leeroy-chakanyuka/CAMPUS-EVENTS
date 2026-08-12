package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.campus_events.domain.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
