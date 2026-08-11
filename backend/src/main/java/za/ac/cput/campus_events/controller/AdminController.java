package za.ac.cput.campus_events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.CreateAdminResponseDTO;
import za.ac.cput.campus_events.DTO.CreateAdminRequestDTO;
import za.ac.cput.campus_events.service.IAdminService;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @GetMapping("/system-status")
    public Map<String, Boolean> systemStatus() {
        return Map.of("initialized", adminService.isSystemInitialized());
    }

    @PostMapping("/seed")
    public CreateAdminResponseDTO seed(@RequestBody CreateAdminRequestDTO request) {
        return adminService.seedAdmin(request);
    }

    @PostMapping
    public CreateAdminResponseDTO createAdmin(@RequestBody CreateAdminRequestDTO request, @RequestParam Long requestingAdminId) {
        return adminService.createAdmin(request, requestingAdminId);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam Long adminId, @RequestParam String currentPassword, @RequestParam String newPassword) {
        adminService.changePassword(adminId, currentPassword, newPassword);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password changed"));
    }
}
