package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.Student;

public interface IStudentService {
    void updateStudentStatus(Long studentId, boolean active, Long requestingAdminId);
    Student save(Student student);
}
