package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Notification;
import java.util.List;
    @Repository
    public interface NotificationRepository extends JpaRepository<Notification, Long> {

        List<Notification> findByStudentIdAndReadFalse(Long studentId);

    }


