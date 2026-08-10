package za.ac.cput.campus_events.factory;

import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.PromoCode;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.domain.Ticket;

public class TicketFactory {
    public static Ticket createTicket(Student student, Event event, PromoCode promoCode, Double price) {

        if (price == null || price < 0) {
            return null;
        }
        if(event == null) {
            return null;
        }
        if(student == null) {
            return null;
        }

       Ticket ouTicket = new Ticket.Builder()
            .setEvent(event)
            .setPromoCode(promoCode)
            .setPrice(price)
            .build();

        return ouTicket;
    }
}
