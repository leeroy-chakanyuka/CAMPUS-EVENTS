package za.ac.cput.campus_events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.campus_events.DTO.RegisterRequestDTO;
import za.ac.cput.campus_events.DTO.RegisterResponseDTO;
import za.ac.cput.campus_events.DTO.ResendRequestDTO;
import za.ac.cput.campus_events.DTO.VerifyRequestDTO;
import za.ac.cput.campus_events.DTO.VerifyResponseDTO;
import za.ac.cput.campus_events.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Optional - useful if your frontend is hosted separately
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody RegisterRequestDTO request) {
        return authService.register(request);
    }

    @PostMapping("/verify")
    public VerifyResponseDTO verify(@RequestBody VerifyRequestDTO request) {
        return authService.verify(request);
    }

    @PostMapping("/resend")
    public RegisterResponseDTO resend(@RequestBody ResendRequestDTO request) {
        return authService.resend(request);
    }
}