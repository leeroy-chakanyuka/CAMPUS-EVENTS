import src.main.java.za.ac.cput.DTO.AdminRequestDTO;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.service.OrganiserService;

@Override
public void createAdmin(AdminRequestDTO dto, Long requestingAdminId) {
    if (requestingAdminId == null) throw new IllegalStateException("Admin only");

    OrganiserService adminRepository = null;
    if (adminRepository.existsByEmail(dto.getEmail())) {
        throw new IllegalArgumentException("Email already exists");
    }

    Admin newAdmin = new Admin.Builder()
            .setFirstName(dto.getFirstName())
            .setLastName(dto.getLastName())
            .setEmail(dto.getEmail())
            .setPassword(dto.getPassword())
            .build();

    adminRepository.save(newAdmin);
}

void main() {
}
