package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.DTO.TicketRequestDTO;
import za.ac.cput.campus_events.domain.Ticket;

import java.util.List;

public interface ITicketService {
    Ticket issue(TicketRequestDTO dto, Long studentId);

    List<Ticket> findByStudent(Long studentId);

    void cancelTicket(Long ticketId, Long studentId);
}
