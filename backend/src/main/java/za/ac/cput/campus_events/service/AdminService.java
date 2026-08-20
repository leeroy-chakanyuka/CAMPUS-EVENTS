package za.ac.cput.campus_events.service;

import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.DTO.FacultyRequestDTO;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.repository.FacultyRepository;
import za.ac.cput.campus_events.repository.OrganiserRepository;
import za.ac.cput.campus_events.repository.StudentRepository;

@Service
public class AdminService implements IAdminService {

    private final FacultyRepository facultyRepository;
    private final OrganiserRepository organiserRepository;
    private final StudentRepository studentRepository;

    public AdminService(FacultyRepository facultyRepository,
                        OrganiserRepository organiserRepository,
                        StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.organiserRepository = organiserRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void createFaculty(FacultyRequestDTO dto, Long adminId) {
        if (adminId == null) throw new IllegalStateException("Admin only");

        if (facultyRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Faculty name already exists");
        }

        Faculty newFaculty = new Faculty.Builder()
                .setName(dto.getName())
                .setContactEmail(dto.getContactEmail())
                .build();

        facultyRepository.save(newFaculty);
    }

    @Override
    public void approveOrganiser(Long organiserId, Long adminId) {
        if (adminId == null) throw new IllegalStateException("Admin only");

        Organiser organiser = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new IllegalArgumentException("Organiser not found"));

        Organiser approved = new Organiser(organiser, true); // immutable copy constructor
        organiserRepository.save(approved);
    }

    @Override
    public void approveStudent(Long studentId, Long adminId) {
        if (adminId == null) throw new IllegalStateException("Admin only");

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Student approved = new Student(student, true); // immutable copy constructor
        studentRepository.save(approved);
    }
}
