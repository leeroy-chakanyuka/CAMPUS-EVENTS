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
    public void issue(TicketRequestDTO dto, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        double finalPrice = dto.getPrice();

        // Validate promo code if provided
        if (dto.getPromoCode() != null && !dto.getPromoCode().isBlank()) {
            PromoCode promo = promoCodeRepository.findByCode(dto.getPromoCode())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid promo code"));

            if (!promo.isActive() || promo.isExpired()) {
                throw new IllegalStateException("Promo code not valid");
            }

            // Apply discount
            finalPrice = finalPrice - (finalPrice * promo.getDiscountPercentage() / 100);
        }

        Ticket ticket = new Ticket.Builder()
                .setEvent(event)
                .setStudent(student)
                .setPrice(finalPrice)
                .build();

        ticketRepository.save(ticket);
    }
}
