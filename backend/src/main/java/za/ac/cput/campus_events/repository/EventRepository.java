package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find all events linked to a specific Faculty
    List<Event> findByFacultyId(Long facultyId);

    // Find all events that are currently open
    List<Event> findByOpenTrue();
}
