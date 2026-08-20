package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.DTO.TicketRequestDTO;

public interface ITicketService {
    void issue(TicketRequestDTO dto, Long studentId);
}
