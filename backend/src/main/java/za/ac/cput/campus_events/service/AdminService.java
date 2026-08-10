package za.ac.cput.campus_events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.ac.cput.DTO.CreateAdminResponseDTO;
import za.ac.cput.campus_events.DTO.CreateAdminRequestDTO;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.factory.AdminFactory;
import za.ac.cput.campus_events.repository.AdminRepository;

import java.util.Optional;

@Service
public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public CreateAdminResponseDTO seedAdmin(CreateAdminRequestDTO request) {
        if (isSystemInitialized()) {
            return new CreateAdminResponseDTO(false, "System already has an admin", null);
        }

        Admin admin = AdminFactory.createAdmin(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword());

        if (admin == null) {
            return new CreateAdminResponseDTO(false, "Invalid admin details", null);
        }

        Admin saved = adminRepository.save(admin);
        return new CreateAdminResponseDTO(true, "Admin created", saved.getId());
    }

    @Override
    public boolean isSystemInitialized() {
        return adminRepository.count() > 0;
    }

    @Override
    public CreateAdminResponseDTO createAdmin(CreateAdminRequestDTO request, Long requestingAdminId) {
        if (requestingAdminId == null) {
            return new CreateAdminResponseDTO(false, "Admin only", null);
        }
        if (adminRepository.existsByEmail(request.getEmail())) {
            return new CreateAdminResponseDTO(false, "An admin with this email already exists", null);
        }

        Admin admin = AdminFactory.createAdmin(request.getFirstName(),
                request.getLastName(), request.getEmail(), request.getPassword());

        if (admin == null) {
            return new CreateAdminResponseDTO(false, "Invalid admin details", null);
        }

        Admin saved = adminRepository.save(admin);
        return new CreateAdminResponseDTO(true, "Admin created", saved.getId());
    }

    @Override
    public void changePassword(Long adminId, String currentPassword, String newPassword) {
       //need to come back and figure this one out
    }
    @Override
    public Optional<Admin> authenticate(String email, String password) {
        return adminRepository.findByEmailAndPassword(email, password);
    }
}
