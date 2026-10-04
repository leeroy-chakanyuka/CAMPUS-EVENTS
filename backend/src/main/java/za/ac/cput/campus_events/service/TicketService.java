package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.DTO.TicketRequestDTO;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.PromoCode;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.domain.Ticket;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.repository.PromoCodeRepository;
import za.ac.cput.campus_events.repository.StudentRepository;
import za.ac.cput.campus_events.repository.TicketRepository;

@Service
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;
    private final PromoCodeRepository promoCodeRepository;

    public TicketService(TicketRepository ticketRepository,
                         EventRepository eventRepository,
                         StudentRepository studentRepository,
                         PromoCodeRepository promoCodeRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.studentRepository = studentRepository;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public Ticket issue(TicketRequestDTO dto, Long studentId) {
        if (dto == null) {
            throw new IllegalArgumentException("Ticket request is required");
        }
        if (studentId == null) {
            throw new IllegalArgumentException("Student id is required");
        }
        if (dto.getEventId() == null) {
            throw new IllegalArgumentException("Event id is required");
        }
        if (dto.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        double finalPrice = dto.getPrice();
        PromoCode promo = null;

        // Validate promo code if provided
        if (dto.getPromoCode() != null && !dto.getPromoCode().isBlank()) {
            PromoCode found = promoCodeRepository.findByCode(dto.getPromoCode().trim())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));

            if (!found.isValidNow()) {
                throw new IllegalStateException("Promo code not valid");
            }
            if (found.getTimesUsed() >= found.getMaxRedemptions()) {
                throw new IllegalStateException("Promo code redemption limit reached");
            }

            // Apply discount (FLAT or PERCENTAGE, clamped at zero)
            finalPrice = applyDiscount(finalPrice, found);

            // Record redemption before issuing the ticket
            PromoCode used = new PromoCode.Builder()
                    .copy(found)
                    .setTimesUsed(found.getTimesUsed() + 1)
                    .build();
            promo = promoCodeRepository.save(used);
        }

        Ticket ticket = new Ticket.Builder()
                .setEvent(event)
                .setStudent(student)
                .setPromoCode(promo)
                .setPrice(finalPrice)
                .setCreatedAt(new java.util.Date())
                .build();

        return ticketRepository.save(ticket);
    }

    @Override
    public java.util.List<Ticket> findByStudent(Long studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student id is required");
        }
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new IllegalArgumentException("Student not found");
        }
        return ticketRepository.findByStudentId(studentId);
    }

    @Override
    public void cancelTicket(Long ticketId, Long studentId) {
        if (ticketId == null) {
            throw new IllegalArgumentException("Ticket id is required");
        }
        if (studentId == null) {
            throw new IllegalArgumentException("Student id is required");
        }
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        if (ticket.getStudent() == null || !studentId.equals(ticket.getStudent().getId())) {
            throw new IllegalArgumentException("Ticket does not belong to this student");
        }
        ticketRepository.delete(ticket);
    }

    private double applyDiscount(double originalPrice, PromoCode promo) {
        if ("FLAT".equalsIgnoreCase(promo.getDiscountType())) {
            return Math.max(0, originalPrice - promo.getValue());
        }
        double pct = promo.getDiscountPercentage();
        if (pct < 0) pct = 0;
        if (pct > 100) pct = 100;
        return Math.max(0, originalPrice - (originalPrice * pct / 100));
    }
}
