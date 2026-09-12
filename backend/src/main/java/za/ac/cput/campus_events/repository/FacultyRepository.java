package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.ac.cput.campus_events.domain.Faculty;
<<<<<<< HEAD

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    boolean existsByName(String name);
=======
import za.ac.cput.campus_events.domain.Organiser;
import java.util.List;
import java.util.Optional;

@Repository

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAll();
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
}
