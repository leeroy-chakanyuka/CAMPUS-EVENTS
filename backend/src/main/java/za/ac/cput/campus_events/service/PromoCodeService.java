package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.PromoCode;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.domain.Event;

@Service
public class PromoCodeService implements IPromoCodeService {

    @Override
    public boolean validateForStudent(PromoCode promoCode, Student student) {
        if (promoCode == null || student == null) return false;
        if (!promoCode.isActive() || promoCode.isExpired()) return false;

        // Optional: check if student already used this promo
        // e.g., promoCode.hasBeenUsedBy(student)

        return true;
    }

    @Override
    public double applyTo(PromoCode promoCode, Event event, double originalPrice) {
        if (promoCode == null || !promoCode.isActive() || promoCode.isExpired()) {
            return originalPrice;
        }

        // Example: percentage discount
        return originalPrice - (originalPrice * promoCode.getDiscountPercentage() / 100);
    }
}
