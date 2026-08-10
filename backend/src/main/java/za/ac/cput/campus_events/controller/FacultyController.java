package za.ac.cput.campus_events.controller;
/*
Mologadi Dikgale
Student Number: 231016263
 */
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.FacultyRequestDTO;
import za.ac.cput.campus_events.DTO.StatusUpdateRequestDTO;
import za.ac.cput.campus_events.service.IFacultyService;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final IFacultyService facultyService;

    public FacultyController(IFacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyRequestDTO dto) {
        try {
            return ResponseEntity.ok(
                    facultyService.createFaculty(dto, dto.getAdminId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateFacultyStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO dto) {
        try {
            facultyService.updateFacultyStatus(
                    id, dto.isActive(), dto.getRequestingAdminId());
            return ResponseEntity.ok("Faculty status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
