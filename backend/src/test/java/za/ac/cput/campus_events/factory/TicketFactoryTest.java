package za.ac.cput.campus_events.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.PromoCode;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.domain.Ticket;

import static org.junit.jupiter.api.Assertions.*;

class TicketFactoryTest {

    private final Student student = new Student.Builder().build();
    private final Event event = new Event.Builder().build();
    private final PromoCode promoCode = new PromoCode.Builder().build();

    @Test
    void createTicketSuccess() {
        Ticket ticket = TicketFactory.createTicket(student, event, promoCode, 150.0);

        assertNotNull(ticket);
        assertEquals(event, ticket.getEvent());
        assertEquals(promoCode, ticket.getPromoCode());
        assertEquals(150.0, ticket.getPrice());
    }

    @Test
    void createTicketWithNegativePrice() {
        Ticket ticket = TicketFactory.createTicket(student, event, promoCode, -50.0);

        assertNull(ticket);
    }
    @Test
    void createTicketWithNullPrice() {
        Ticket ticket = TicketFactory.createTicket(student, event, promoCode, null);

        assertNull(ticket);
    }
    @Test
    void createTicketWithNullStudent() {
        Ticket ticket = TicketFactory.createTicket(null, event, promoCode, 150.0);

        assertNull(ticket);
    }

    @Test
    void createTicketWithNullEvent() {
        Ticket ticket = TicketFactory.createTicket(student, null, promoCode, 150.0);

        assertNull(ticket);
    }

    @Test
    void createTicketWithNullPromoCode() {
        Ticket ticket = TicketFactory.createTicket(student, event, null, 150.0);

        assertNotNull(ticket);
        assertNull(ticket.getPromoCode());
    }

    @Test
    void createTicketWithZeroPrice() {
        Ticket ticket = TicketFactory.createTicket(student, event, promoCode, 0.0);

        assertNotNull(ticket);
        assertEquals(0.0, ticket.getPrice());
    }
}
