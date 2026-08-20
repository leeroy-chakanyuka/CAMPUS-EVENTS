package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.campus_events.domain.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    boolean existsByName(String name);
}
