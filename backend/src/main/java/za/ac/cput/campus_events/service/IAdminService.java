package za.ac.cput.campus_events.service;

<<<<<<< HEAD
import za.ac.cput.campus_events.DTO.FacultyRequestDTO;

public interface IAdminService {
    void createFaculty(FacultyRequestDTO dto, Long adminId);
    void approveOrganiser(Long organiserId, Long adminId);
    void approveStudent(Long studentId, Long adminId);
=======
import za.ac.cput.campus_events.DTO.CreateAdminRequestDTO;
import za.ac.cput.campus_events.DTO.CreateAdminResponseDTO;
import za.ac.cput.campus_events.domain.Admin;

import java.util.Optional;

public interface IAdminService {

    CreateAdminResponseDTO seedAdmin(CreateAdminRequestDTO request);
    // does the system have more than one admin
    boolean isSystemInitialized();
    CreateAdminResponseDTO createAdmin(CreateAdminRequestDTO request, Long requestingAdminId);
    void changePassword(Long adminId, String currentPassword, String newPassword);
    Optional<Admin> authenticate(String email, String password);
>>>>>>> 3910e098ee96ddd014a3a56a0d2d920a85fa89af
}
