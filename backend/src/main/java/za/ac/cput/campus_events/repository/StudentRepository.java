package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByStudentNumber(String studentNumber);

    Student findStudentByEmailAndPassword(String email, String password);
    Student findStudentByFaculty(Faculty faculty);
}
