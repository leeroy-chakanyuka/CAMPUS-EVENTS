package za.ac.cput.campus_events.controller;
/*
Mologadi Dikgale
Student Number: 231016263
 */
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.OrganiserResponseDTO;
import za.ac.cput.campus_events.DTO.StatusUpdateRequestDTO;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.service.IOrganiserService;

import java.util.List;

@RestController
@RequestMapping("/organiser")
public class OrganiserController {
    private final IOrganiserService organiserService;

    public OrganiserController(IOrganiserService organiserService) {
        this.organiserService = organiserService;
    }

    @GetMapping
    public ResponseEntity<List<OrganiserResponseDTO>> allOrganisers() {
        return ResponseEntity.ok(organiserService.findAll().stream().map(this::toResponse).toList());
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

    private OrganiserResponseDTO toResponse(Organiser organiser) {
        OrganiserResponseDTO dto = new OrganiserResponseDTO();
        dto.setId(organiser.getId());
        dto.setFirstName(organiser.getFirstName());
        dto.setLastName(organiser.getLastName());
        dto.setEmail(organiser.getEmail());
        dto.setFacultyName(organiser.getFaculty() == null ? null : organiser.getFaculty().getName());
        dto.setActive(organiser.isActive());
        return dto;
    }
}
