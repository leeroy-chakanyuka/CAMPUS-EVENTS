package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.domain.PromoCode;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.domain.Event;

public interface IPromoCodeService {

    /**
     * Validates whether a promo code can be used by a given student.
     * Checks expiry, active status, and any student-specific restrictions.
     *
     * @param promoCode the promo code to validate
     * @param student the student attempting to use the promo
     * @return true if valid, false otherwise
     */
    boolean validateForStudent(PromoCode promoCode, Student student);

    /**
     * Applies a promo code to an event ticket purchase.
     * Adjusts the price based on discount percentage or fixed value.
     *
     * @param promoCode the promo code being applied
     * @param event the event the ticket belongs to
     * @param originalPrice the original ticket price
     * @return the discounted price
     */
    double applyTo(PromoCode promoCode, Event event, double originalPrice);
}
