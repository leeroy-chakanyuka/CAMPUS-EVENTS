package za.ac.cput.campus_events.service;
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.factory.AdminFactory;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Student;
import za.ac.cput.campus_events.repository.AdminRepository;
import za.ac.cput.campus_events.repository.FacultyRepository;
import za.ac.cput.campus_events.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.campus_events.DTO.*;
import za.ac.cput.campus_events.DTO.RegisterResponseDTO;
import za.ac.cput.campus_events.DTO.ResendRequestDTO;
import za.ac.cput.campus_events.DTO.VerifyRequestDTO;
import za.ac.cput.campus_events.DTO.VerifyResponseDTO;
import za.ac.cput.campus_events.domain.PendingRegistration;
import za.ac.cput.campus_events.repository.OrganiserRepository;
import za.ac.cput.campus_events.repository.PendingRegistrationRepository;
import za.ac.cput.campus_events.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final StudentRepository studentRepository;
    private final OrganiserRepository organiserRepository;
    private final EmailService emailService;
    private final AdminRepository adminRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public AuthService(PendingRegistrationRepository pendingRegistrationRepository,
                       StudentRepository studentRepository,
                       OrganiserRepository organiserRepository, EmailService emailService,
                       AdminRepository adminRepository, FacultyRepository facultyRepository) {

        this.pendingRegistrationRepository = pendingRegistrationRepository;
        this.studentRepository = studentRepository;
        this.organiserRepository = organiserRepository;
        this.emailService = emailService;
        this.adminRepository = adminRepository;
        this.facultyRepository = facultyRepository;
    }

    /**
     * Step 1:
     * Accept registration details.
     * Generate a PIN.
     * Save as PendingRegistration.
     */
    public RegisterResponseDTO register(RegisterRequestDTO request) {

        RegisterResponseDTO response = new RegisterResponseDTO();

        if (request == null || request.getEmail() == null || request.getEmail().isBlank() || request.getPassword() == null || request.getPassword().isBlank() || request.getRole() == null || request.getRole().isBlank()) {
            response.setSuccess(false);
            response.setMessage("Invalid registration details.");
            return response;
        }
        // Check if student email already exists
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setSuccess(false);
            response.setMessage("A student account with this email already exists.");
            return response;
        }

        // Check if organiser email already exists
        if (organiserRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setSuccess(false);
            response.setMessage("An organiser account with this email already exists.");
            return response;
        }

        if (adminRepository.existsByEmail(request.getEmail())) {
            response.setSuccess(false);
            response.setMessage("An admin account with this email already exists.");
            return response;
        }

        // Check if there is already a pending registration
        // If the previous registration has expired, it automatically deletes it and lets the user register again.
        PendingRegistration existingPending =
                pendingRegistrationRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingPending != null) {

            if (existingPending.isExpired()) {
                pendingRegistrationRepository.delete(existingPending);
            } else {
                response.setSuccess(false);
                response.setMessage("A verification request for this email already exists.");
                return response;
            }
        }

        // Student numbers must also be unique
        if ("STUDENT".equalsIgnoreCase(request.getRole())
                && request.getStudentNumber() != null
                && !request.getStudentNumber().isBlank()
                && studentRepository.findByStudentNumber(request.getStudentNumber()).isPresent()) {

            response.setSuccess(false);
            response.setMessage("Student number is already registered.");
            return response;
        }

        String pin = generatePin();

        PendingRegistration pendingRegistration =
                new PendingRegistration.Builder()
                        .setFirstName(request.getFirstName())
                        .setLastName(request.getLastName())
                        .setEmail(request.getEmail())
                        .setPassword(request.getPassword())
                        .setRole(request.getRole())
                        .setFacultyId(request.getFacultyId())
                        .setStudentNumber(request.getStudentNumber())
                        .setPin(pin)
                        .build();

        pendingRegistrationRepository.save(pendingRegistration);

        emailService.sendVerificationEmail(
                pendingRegistration.getEmail(),
                pin
        );

        response.setSuccess(true);
        response.setMessage("Registration successful. Please verify your account.");
        response.setUuid(pendingRegistration.getUuid());
        return response;
    }

    /**
     * Step 2:
     * Verify PIN, then create the real Student or Organiser account
     * from the PendingRegistration data.
     */
    public VerifyResponseDTO verify(VerifyRequestDTO request) {

        VerifyResponseDTO response = new VerifyResponseDTO();

        if (request == null
                || request.getUuid() == null || request.getUuid().isBlank()
                || request.getPin() == null || request.getPin().isBlank()) {

            response.setSuccess(false);
            response.setMessage("Invalid verification request.");
            return response;
        }

        PendingRegistration pendingRegistration =
                pendingRegistrationRepository
                        .findById(request.getUuid())
                        .orElse(null);

        if (pendingRegistration == null) {
            response.setSuccess(false);
            response.setMessage("Registration request not found.");
            return response;
        }

        if (pendingRegistration.isExpired()) {
            response.setSuccess(false);
            response.setMessage("Verification PIN has expired.");
            return response;
        }

        if (!pendingRegistration.getPin().equals(request.getPin())) {
            response.setSuccess(false);
            response.setMessage("Incorrect PIN.");
            return response;
        }

        String role = pendingRegistration.getRole().toUpperCase();

        // Resolve the faculty, if one was supplied at registration time
        Faculty faculty = null;
        if (pendingRegistration.getFacultyId() != null) {
            faculty = facultyRepository.findById(pendingRegistration.getFacultyId()).orElse(null);
            if (faculty == null) {
                response.setSuccess(false);
                response.setMessage("Faculty not found.");
                return response;
            }
        }

        if ("STUDENT".equals(role)) {
            Student student = new Student.Builder()
                    .setFirstName(pendingRegistration.getFirstName())
                    .setLastName(pendingRegistration.getLastName())
                    .setEmail(pendingRegistration.getEmail())
                    .setPassword(pendingRegistration.getPassword())
                    .setStudentNumber(pendingRegistration.getStudentNumber())
                    .setFaculty(faculty)
                    .build();
            Student savedStudent = studentRepository.save(student);
            response.setAccountId(savedStudent.getId());

        } else if ("ORGANISER".equals(role)) {
            Organiser organiser = new Organiser.Builder()
                    .setFirstName(pendingRegistration.getFirstName())
                    .setLastName(pendingRegistration.getLastName())
                    .setEmail(pendingRegistration.getEmail())
                    .setPassword(pendingRegistration.getPassword())
                    .setRole(role)
                    .setFaculty(faculty)
                    .setCreatedAt(LocalDateTime.now())
                    .build();
            Organiser savedOrganiser = organiserRepository.save(organiser);
            response.setAccountId(savedOrganiser.getId());

        } else if ("ADMIN".equals(role)) {
            Admin admin = AdminFactory.createAdmin(
                    pendingRegistration.getFirstName(),
                    pendingRegistration.getLastName(),
                    pendingRegistration.getEmail(),
                    pendingRegistration.getPassword());

            if (admin == null) {
                response.setSuccess(false);
                response.setMessage("Invalid admin details.");
                return response;
            }

            if (adminRepository.existsByEmail(admin.getEmail())) {
                response.setSuccess(false);
                response.setMessage("Admin account already exists.");
                return response;
            }

            Admin savedAdmin = adminRepository.save(admin);
            response.setAccountId(savedAdmin.getId());

        } else {
            response.setSuccess(false);
            response.setMessage("Unsupported role for account creation.");
            return response;
        }

        pendingRegistrationRepository.delete(pendingRegistration);

        response.setSuccess(true);
        response.setMessage("Account verified successfully.");
        response.setRole(pendingRegistration.getRole());

        return response;
    }

    /**
     * Step 3:
     * Generate a new PIN.
     */
    public RegisterResponseDTO resend(ResendRequestDTO request) {

        RegisterResponseDTO response = new RegisterResponseDTO();

        if (request == null
                || request.getUuid() == null
                || request.getUuid().isBlank()) {

            response.setSuccess(false);
            response.setMessage("Invalid resend request.");
            return response;
        }

        PendingRegistration pendingRegistration =
                pendingRegistrationRepository
                        .findById(request.getUuid())
                        .orElse(null);

        if (pendingRegistration == null) {
            response.setSuccess(false);
            response.setMessage("Registration request not found.");
            return response;
        }

        String newPin = generatePin();

        // Update the existing registration
        PendingRegistration updatedPendingRegistration = pendingRegistration
            .withPin(newPin)
            .withExpiresAt(LocalDateTime.now().plusMinutes(10));

        pendingRegistrationRepository.save(updatedPendingRegistration);

        emailService.sendVerificationEmail(
            updatedPendingRegistration.getEmail(),
                newPin
        );

        response.setSuccess(true);
        response.setMessage("New PIN generated.");

        return response;
    }

    private String generatePin() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        LoginResponseDTO response = new LoginResponseDTO();

        if (request == null || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()
                || request.getRole() == null || request.getRole().isBlank()) {
            response.setSuccess(false);
            response.setMessage("Invalid login request.");
            return response;
        }

        String role = request.getRole().toUpperCase();
        String email = request.getEmail();
        String rawPassword = request.getPassword();

        // Depending on role, query the correct repository
        if ("STUDENT".equals(role)) {
            Optional<Student> optional = studentRepository.findByEmail(email);
            if (optional.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Student account not found.");
                return response;
            }
            Student student = optional.get();
            if (!student.getPassword().equals(rawPassword)) {
                response.setSuccess(false);
                response.setMessage("Incorrect password.");
                return response;
            }
            if (!student.isActive()) {
                response.setSuccess(false);
                response.setMessage("Account is disabled.");
                return response;
            }
            response.setSuccess(true);
            response.setMessage("Login successful.");
            response.setAccountId(student.getId());
            response.setRole("STUDENT");
            return response;
        }

        if ("ORGANISER".equals(role)) {
            Optional<Organiser> optional = organiserRepository.findByEmail(email);
            if (optional.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Organiser account not found.");
                return response;
            }
            Organiser organiser = optional.get();
            if (!organiser.getPassword().equals(rawPassword)) {
                response.setSuccess(false);
                response.setMessage("Incorrect password.");
                return response;
            }
            if (!organiser.isActive()) {
                response.setSuccess(false);
                response.setMessage("Account is disabled.");
                return response;
            }
            response.setSuccess(true);
            response.setMessage("Login successful.");
            response.setAccountId(organiser.getId());
            response.setRole("ORGANISER");
            return response;
        }

        if ("ADMIN".equals(role)) {
            Optional<Admin> optional = adminRepository.findByEmail(email);
            if (optional.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Admin account not found.");
                return response;
            }
            Admin admin = optional.get();
            if (!admin.getPassword().equals(rawPassword)) {
                response.setSuccess(false);
                response.setMessage("Incorrect password.");
                return response;
            }

            response.setSuccess(true);
            response.setMessage("Login successful.");
            response.setAccountId(admin.getId());
            response.setRole("ADMIN");
            return response;
        }

        response.setSuccess(false);
        response.setMessage("Unsupported role.");
        return response;
    }
}