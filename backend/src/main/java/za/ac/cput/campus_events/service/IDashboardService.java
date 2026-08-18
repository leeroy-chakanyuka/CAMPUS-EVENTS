package za.ac.cput.campus_events.service;
/*
Mologadi Dikgale
Student No: 231016263
 */
import za.ac.cput.campus_events.domain.Admin;
import za.ac.cput.campus_events.domain.Event;
import za.ac.cput.campus_events.domain.Faculty;
import za.ac.cput.campus_events.domain.Notification;
import za.ac.cput.campus_events.domain.Organiser;
import za.ac.cput.campus_events.domain.Student;

import java.util.List;

public interface IDashboardService {


    Faculty createFaculty(String name, String contactEmail, Long adminId);
    void    updateFacultyStatus(Long facultyId, boolean active, Long requestingAdminId);
    List<Faculty> getAllFaculties();


    void          updateStudentStatus(Long studentId, boolean active, Long requestingAdminId);
    List<Student> getAllStudents();

    void            updateOrganiserStatus(Long organiserId, boolean active, Long requestingAdminId);
    List<Organiser> getAllOrganisers();


    void         forceCancelEvent(Long eventId, Long requestingAdminId);
    List<Event>  getAllEvents();


    Admin       createAdmin(String firstName, String lastName, String email,
                            String temporaryPassword, Long requestingAdminId);
    void        changePassword(Long adminId, String currentPassword, String newPassword);
    List<Admin> getAllAdmins();

    void sendNotification(String recipientType, Long recipientId,
                          String recipient, String message);
}