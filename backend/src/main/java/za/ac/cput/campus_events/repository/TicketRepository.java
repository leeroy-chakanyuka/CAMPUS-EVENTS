package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    java.util.List<Ticket> findByStudentId(Long studentId);

    boolean existsByStudentIdAndEventId(Long studentId, Long eventId);

    long countByEventId(Long eventId);
}
