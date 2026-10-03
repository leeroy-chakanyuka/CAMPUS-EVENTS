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
        if (!promoCode.isValidNow()) return false;
        if (promoCode.getTimesUsed() >= promoCode.getMaxRedemptions()) return false;


        return true;
    }

    @Override
    public double applyTo(PromoCode promoCode, Event event, double originalPrice) {
        if (promoCode == null || !promoCode.isValidNow()) {
            return originalPrice;
        }
        if (promoCode.getTimesUsed() >= promoCode.getMaxRedemptions()) {
            return originalPrice;
        }

        if ("FLAT".equalsIgnoreCase(promoCode.getDiscountType())) {
            return Math.max(0, originalPrice - promoCode.getValue());
        }

        double pct = promoCode.getDiscountPercentage();
        if (pct < 0) pct = 0;
        if (pct > 100) pct = 100;
        return Math.max(0, originalPrice - (originalPrice * pct / 100));
    }
}
