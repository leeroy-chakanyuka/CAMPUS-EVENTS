package za.ac.cput.campus_events.service;
/*
Mologadi Dikgale
Student No: 231016263
 */

import za.ac.cput.campus_events.DTO.FacultyRequestDTO;
import za.ac.cput.campus_events.domain.Faculty;
import java.util.List;
import java.util.Optional;

public interface IFacultyService extends Iservice<Faculty, Long>{
    Faculty save(Faculty faculty);
    Optional<Faculty> findById(Long id);
    List<Faculty> findAll();
    void deleteById(Long id);
    void deactivate(Long facultyId);
    List<Faculty> findByStatus(String status);
    Faculty createFaculty(FacultyRequestDTO dto, Long adminId);
    void updateFacultyStatus(Long facultyId, boolean active, Long adminId);
}
