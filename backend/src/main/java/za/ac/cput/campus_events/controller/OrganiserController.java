package za.ac.cput.campus_events.controller;
/*
Mologadi Dikgale
Student Number: 231016263
 */
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.StatusUpdateRequestDTO;
import za.ac.cput.campus_events.service.IOrganiserService;

@RestController
@RequestMapping("/organiser")
public class OrganiserController {
    private final IOrganiserService organiserService;

    public OrganiserController(IOrganiserService organiserService) {
        this.organiserService = organiserService;
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrganiserStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO dto) {
        try {
            organiserService.updateOrganiserStatus(
                    id, dto.isActive(), dto.getRequestingAdminId());
            return ResponseEntity.ok("Organiser status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
