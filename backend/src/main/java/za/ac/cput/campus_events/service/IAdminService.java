package za.ac.cput.campus_events.service;

import za.ac.cput.campus_events.DTO.FacultyRequestDTO;

public interface IAdminService {
    void createFaculty(FacultyRequestDTO dto, Long adminId);
    void approveOrganiser(Long organiserId, Long adminId);
    void approveStudent(Long studentId, Long adminId);
}
