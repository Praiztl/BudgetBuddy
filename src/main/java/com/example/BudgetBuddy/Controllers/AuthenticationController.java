package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a new Admin.
     */
    @PostMapping("/signup/admin")
    public ResponseEntity<AdminResponseDTO> registerAdmin(@Valid @RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        return authenticationService.registerAdmin(adminRegistrationDTO);
    }

    /**
     * Registers a new HOD.
     */
    @PostMapping("/signup/hod")
    public ResponseEntity<HODResponseDTO> registerHOD(@Valid @RequestBody HODRegistrationDTO hodRegistrationDTO) {
        return authenticationService.registerHOD(hodRegistrationDTO);
    }

    /**
     * Handles user login for both Admins & HODs.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }
}
