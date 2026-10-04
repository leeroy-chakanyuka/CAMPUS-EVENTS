package za.ac.cput.campus_events.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.TicketRequestDTO;
import za.ac.cput.campus_events.DTO.TicketResponseDTO;
import za.ac.cput.campus_events.domain.Ticket;
import za.ac.cput.campus_events.service.ITicketService;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final ITicketService ticketService;

    public TicketController(ITicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<?> issueTicket(@RequestBody TicketRequestDTO dto,
                                        @RequestParam Long studentId) {
        try {
            Ticket saved = ticketService.issue(dto, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> ticketsForStudent(@PathVariable Long studentId) {
        try {
            List<TicketResponseDTO> tickets = ticketService.findByStudent(studentId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelTicket(@PathVariable Long id,
                                         @RequestParam Long studentId) {
        try {
            ticketService.cancelTicket(id, studentId);
            return ResponseEntity.ok("Ticket cancelled");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private TicketResponseDTO toResponse(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setPrice(ticket.getPrice());
        if (ticket.getEvent() != null) {
            dto.setEventId(ticket.getEvent().getId());
            dto.setEventTitle(ticket.getEvent().getTitle());
            dto.setEventDate(ticket.getEvent().getEventDate() == null
                    ? null : ticket.getEvent().getEventDate().toString());
            if (ticket.getEvent().getVenue() != null) {
                dto.setVenueName(ticket.getEvent().getVenue().getName());
            }
        }
        if (ticket.getStudent() != null) {
            dto.setStudentId(ticket.getStudent().getId());
        }
        if (ticket.getPromoCode() != null) {
            dto.setPromoCode(ticket.getPromoCode().getCode());
        }
        if (ticket.getCreatedAt() != null) {
            dto.setCreatedAt(ticket.getCreatedAt().toString());
        }
        return dto;
    }
}
