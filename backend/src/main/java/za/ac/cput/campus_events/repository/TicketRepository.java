package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
