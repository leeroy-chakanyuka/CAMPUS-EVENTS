package za.ac.cput.campus_events.service;
/*
Mologadi Dikgale
Student No: 231016263
 */
import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.repository.AdminRepository;
import za.ac.cput.campus_events.repository.EventRepository;
import za.ac.cput.campus_events.repository.FacultyRepository;
import za.ac.cput.campus_events.repository.NotificationRepository;
import za.ac.cput.campus_events.repository.OrganiserRepository;
import za.ac.cput.campus_events.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService implements IDashboardService {

    private final FacultyRepository    facultyRepository;
    private final StudentRepository    studentRepository;
    private final OrganiserRepository  organiserRepository;
    private final EventRepository      eventRepository;
    private final AdminRepository      adminRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(FacultyRepository facultyRepository,
                            StudentRepository studentRepository,
                            OrganiserRepository organiserRepository,
                            EventRepository eventRepository,
                            AdminRepository adminRepository,
                            NotificationRepository notificationRepository) {
        this.facultyRepository      = facultyRepository;
        this.studentRepository      = studentRepository;
        this.organiserRepository    = organiserRepository;
        this.eventRepository        = eventRepository;
        this.adminRepository        = adminRepository;
        this.notificationRepository = notificationRepository;
    }



    @Override
    public Faculty createFaculty(String name, String contactEmail, Long adminId) {
        if (adminId == null)
            throw new IllegalStateException("Admin only");
        if (name == null || name.trim().isEmpty())
            throw new RuntimeException("Faculty name is required");
        if (contactEmail == null || !contactEmail.contains("@"))
            throw new RuntimeException("Valid contact email is required");

        facultyRepository.findByName(name).ifPresent(f -> {
            throw new RuntimeException("Faculty with name '" + name + "' already exists");
        });

        Faculty faculty = new Faculty.Builder()
                .name(name)
                .contactEmail(contactEmail)
                .status("ACTIVE")
                .createdByAdminId(adminId)
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        return facultyRepository.save(faculty);
    }

    @Override
    public void updateFacultyStatus(Long facultyId, boolean active, Long requestingAdminId) {
        if (requestingAdminId == null)
            throw new IllegalStateException("Admin only");

        Faculty existing = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found: " + facultyId));

        Faculty updated = new Faculty(existing, active);
        facultyRepository.save(updated);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    // ── STUDENT ───────────────────────────────────────────────────────────

    @Override
    public void updateStudentStatus(Long studentId, boolean active, Long requestingAdminId) {
        if (requestingAdminId == null)
            throw new IllegalStateException("Admin only");

        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        Student updated = new Student(existing, active);
        studentRepository.save(updated);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }



    @Override
    public void updateOrganiserStatus(Long organiserId, boolean active, Long requestingAdminId) {
        if (requestingAdminId == null)
            throw new IllegalStateException("Admin only");

        Organiser existing = organiserRepository.findById(organiserId)
                .orElseThrow(() -> new RuntimeException("Organiser not found: " + organiserId));

        Organiser updated = new Organiser(existing, active);
        organiserRepository.save(updated);
    }

    @Override
    public List<Organiser> getAllOrganisers() {
        return organiserRepository.findAll();
    }


    @Override
    public void forceCancelEvent(Long eventId, Long requestingAdminId) {
        if (requestingAdminId == null)
            throw new IllegalStateException("Admin only");

        Event existing = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        existing.closeRegistration();
        eventRepository.save(existing);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }



    @Override
    public Admin createAdmin(String firstName, String lastName, String email,
                             String temporaryPassword, Long requestingAdminId) {
        if (requestingAdminId == null)
            throw new IllegalStateException("Admin only");
        if (firstName == null || firstName.trim().isEmpty())
            throw new RuntimeException("First name is required");
        if (lastName == null || lastName.trim().isEmpty())
            throw new RuntimeException("Last name is required");
        if (email == null || !email.contains("@"))
            throw new RuntimeException("Valid email is required");
        if (temporaryPassword == null || temporaryPassword.trim().isEmpty())
            throw new RuntimeException("Temporary password is required");

        adminRepository.findByEmail(email).ifPresent(a -> {
            throw new RuntimeException("Email already used by another admin");
        });

        Admin admin = new Admin.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(temporaryPassword)
                .createdAt(LocalDateTime.now())
                .build();

        return adminRepository.save(admin);
    }

    @Override
    public void changePassword(Long adminId, String currentPassword, String newPassword) {
        Admin existing = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + adminId));

        if (!existing.getPassword().equals(currentPassword))
            throw new RuntimeException("Current password is incorrect");
        if (newPassword == null || newPassword.trim().isEmpty())
            throw new RuntimeException("New password is required");

        Admin updated = new Admin.Builder()
                .id(existing.getId())
                .firstName(existing.getFirstName())
                .lastName(existing.getLastName())
                .email(existing.getEmail())
                .password(newPassword)
                .createdAt(existing.getCreatedAt())
                .build();

        adminRepository.save(updated);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }


    @Override
    public void sendNotification(String recipientType, Long recipientId,
                                 String recipient, String message) {
        if (message == null || message.trim().isEmpty())
            throw new RuntimeException("Message is required");

        Notification notification = new Notification.Builder()
                .title("Admin Notification")
                .message(message)
                .read(false)
                .createdAt(LocalDateTime.now())
                .studentId(recipientType.equals("Student") ? recipientId : null)
                .build();

        notificationRepository.save(notification);
    }
}