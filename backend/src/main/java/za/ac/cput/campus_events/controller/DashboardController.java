package za.ac.cput.campus_events.controller;
/*
Mologadi Dikgale
Stu no:231016263
 */
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.service.IDashboardService;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final IDashboardService dashboardService;

    public DashboardController(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }



    @GetMapping("/faculty")
    public ResponseEntity<?> getAllFaculties() {
        try {
            return ResponseEntity.ok(dashboardService.getAllFaculties());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/faculty")
    public ResponseEntity<?> createFaculty(@RequestBody Map<String, Object> body) {
        try {
            String name         = (String) body.get("name");
            String contactEmail = (String) body.get("contactEmail");
            Long   adminId      = ((Number) body.get("adminId")).longValue();
            return ResponseEntity.ok(
                    dashboardService.createFaculty(name, contactEmail, adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/faculty/{id}/status")
    public ResponseEntity<?> updateFacultyStatus(@PathVariable Long id,
                                                 @RequestBody Map<String, Object> body) {
        try {
            boolean active             = (Boolean) body.get("active");
            Long    requestingAdminId  = ((Number) body.get("requestingAdminId")).longValue();
            dashboardService.updateFacultyStatus(id, active, requestingAdminId);
            return ResponseEntity.ok("Faculty status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/student")
    public ResponseEntity<?> getAllStudents() {
        try {
            return ResponseEntity.ok(dashboardService.getAllStudents());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/student/{id}/status")
    public ResponseEntity<?> updateStudentStatus(@PathVariable Long id,
                                                 @RequestBody Map<String, Object> body) {
        try {
            boolean active            = (Boolean) body.get("active");
            Long    requestingAdminId = ((Number) body.get("requestingAdminId")).longValue();
            dashboardService.updateStudentStatus(id, active, requestingAdminId);
            return ResponseEntity.ok("Student status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/organiser")
    public ResponseEntity<?> getAllOrganisers() {
        try {
            return ResponseEntity.ok(dashboardService.getAllOrganisers());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/organiser/{id}/status")
    public ResponseEntity<?> updateOrganiserStatus(@PathVariable Long id,
                                                   @RequestBody Map<String, Object> body) {
        try {
            boolean active            = (Boolean) body.get("active");
            Long    requestingAdminId = ((Number) body.get("requestingAdminId")).longValue();
            dashboardService.updateOrganiserStatus(id, active, requestingAdminId);
            return ResponseEntity.ok("Organiser status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/event")
    public ResponseEntity<?> getAllEvents() {
        try {
            return ResponseEntity.ok(dashboardService.getAllEvents());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/event/{id}/force-cancel")
    public ResponseEntity<?> forceCancelEvent(@PathVariable Long id,
                                              @RequestBody Map<String, Object> body) {
        try {
            Long requestingAdminId = ((Number) body.get("requestingAdminId")).longValue();
            dashboardService.forceCancelEvent(id, requestingAdminId);
            return ResponseEntity.ok("Event cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/admin")
    public ResponseEntity<?> getAllAdmins() {
        try {
            return ResponseEntity.ok(dashboardService.getAllAdmins());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, Object> body) {
        try {
            String firstName        = (String) body.get("firstName");
            String lastName         = (String) body.get("lastName");
            String email            = (String) body.get("email");
            String temporaryPassword= (String) body.get("temporaryPassword");
            Long   requestingAdminId= ((Number) body.get("requestingAdminId")).longValue();
            return ResponseEntity.ok(
                    dashboardService.createAdmin(
                            firstName, lastName, email,
                            temporaryPassword, requestingAdminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> body) {
        try {
            Long   adminId         = ((Number) body.get("adminId")).longValue();
            String currentPassword = (String) body.get("currentPassword");
            String newPassword     = (String) body.get("newPassword");
            dashboardService.changePassword(adminId, currentPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/notification/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> body) {
        try {
            String recipientType = (String) body.get("recipientType");
            Long   recipientId   = ((Number) body.get("recipientId")).longValue();
            String recipient     = (String) body.get("recipient");
            String message       = (String) body.get("message");
            dashboardService.sendNotification(
                    recipientType, recipientId, recipient, message);
            return ResponseEntity.ok("Notification sent successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
