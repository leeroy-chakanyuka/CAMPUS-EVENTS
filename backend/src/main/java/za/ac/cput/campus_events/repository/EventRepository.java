package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByFacultyId(Long facultyId);

    List<Event> findByOpenTrue();

    List<Event> findByOrganiserId(Long organiserId);
}
