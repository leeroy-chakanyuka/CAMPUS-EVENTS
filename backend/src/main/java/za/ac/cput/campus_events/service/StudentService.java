package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.repository.StudentRepository;

@Service
public class StudentService implements IStudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void updateStudentStatus(Long studentId, boolean active, Long requestingAdminId) {
        if (requestingAdminId == null) {
            throw new IllegalStateException("Admin only");
        }

        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        Student updated = new Student(existing, active);
        studentRepository.save(updated);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
