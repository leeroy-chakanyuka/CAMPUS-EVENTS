package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.PromoCode;


import java.util.Optional;
@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {

    Optional<PromoCode> findByCode(String code);

    @Query("""
        SELECT COUNT(t) > 0
        FROM Ticket t
        WHERE t.student.id = :studentId
        AND t.promoCode.id = :promoCodeId
        """)
    boolean existsRedemptionByStudentIdAndPromoCodeId(
            @Param("studentId") Long studentId,
            @Param("promoCodeId") Long promoCodeId);

}