package za.ac.cput.campus_events.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.StatusUpdateRequestDTO;
import za.ac.cput.campus_events.DTO.StudentResponseDTO;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.service.IStudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final IStudentService studentService;

    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> allStudents() {
        return ResponseEntity.ok(studentService.findAll().stream().map(this::toResponse).toList());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStudentStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO dto) {
        try {
            studentService.updateStudentStatus(id, dto.isActive(), dto.getRequestingAdminId());
            return ResponseEntity.ok("Student status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private StudentResponseDTO toResponse(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setStudentNumber(student.getStudentNumber());
        dto.setFacultyName(student.getFaculty() == null ? null : student.getFaculty().getName());
        dto.setActive(student.isActive());
        return dto;
    }
}
