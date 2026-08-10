package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.DTO.CreateAdminRequestDTO;
import za.ac.cput.DTO.CreateAdminResponseDTO;
import za.ac.cput.campus_events.domain.Admin;

import java.util.Optional;

public interface IAdminService {

    CreateAdminResponseDTO seedAdmin(CreateAdminRequestDTO request);
    // does the system have more than one admin
    boolean isSystemInitialized();
    CreateAdminResponseDTO createAdmin(CreateAdminRequestDTO request, Long requestingAdminId);
    void changePassword(Long adminId, String currentPassword, String newPassword);
    Optional<Admin> authenticate(String email, String password);
}
