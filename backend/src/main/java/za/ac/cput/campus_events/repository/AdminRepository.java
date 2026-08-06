package za.ac.cput.campus_events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.campus_events.domain.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findAdminByEmailAndPassword(String email, String password);
}
