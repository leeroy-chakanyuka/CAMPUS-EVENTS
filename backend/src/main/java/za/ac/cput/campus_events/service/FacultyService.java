package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.DTO.FacultyRequestDTO;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.factory.FacultyFactory;
import za.ac.cput.campus_events.repository.AdminRepository;
import za.ac.cput.campus_events.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService implements IFacultyService {

    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;

    public FacultyService(FacultyRepository facultyRepository, AdminRepository adminRepository) {
        this.facultyRepository = facultyRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        return facultyRepository.findById(id);
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public void deactivate(Long facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new IllegalStateException("Faculty not found"));
        facultyRepository.save(new Faculty(faculty, false));
    }

    @Override
    public List<Faculty> findByStatus(String status) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.isActive() == Boolean.parseBoolean(status))
                .toList();
    }

    @Override
    public Faculty createFaculty(FacultyRequestDTO dto, Long adminId) {
        if (adminId == null) {
            throw new IllegalStateException("Admin only");
        }

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalStateException("Admin not found"));

        if (dto == null || dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalStateException("Faculty name is required");
        }

        if (dto.getContactEmail() == null || dto.getContactEmail().isBlank()) {
            throw new IllegalStateException("Contact email is required");
        }

        if (facultyRepository.findAll().stream().anyMatch(f -> f.getName().equalsIgnoreCase(dto.getName()))) {
            throw new IllegalStateException("Faculty already exists");
        }

        Faculty faculty = FacultyFactory.createFaculty(
                dto.getName(),
                "ACTIVE",
                dto.getContactEmail(),
                admin
        );

        return facultyRepository.save(faculty);
    }

    @Override
    public void updateFacultyStatus(Long facultyId, boolean active, Long adminId) {
        if (adminId == null) {
            throw new IllegalStateException("Admin only");
        }

        if (adminRepository.findById(adminId).isEmpty()) {
            throw new IllegalStateException("Admin only");
        }

        Faculty existing = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new IllegalStateException("Faculty not found"));

        Faculty updated = new Faculty(existing, active);
        facultyRepository.save(updated);
    }

    @Override
    public <T> T create(T t) {
        return null;
    }

    @Override
    public <T> T read(Long id) {
        return null;
    }

    @Override
    public <T> T update(T t) {
        return null;
    }

    @Override
    public <T> void delete(T t) {

    }

}
