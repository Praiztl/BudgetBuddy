package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Services.PasswordService;
import com.example.BudgetBuddy.Services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * Handles OTPCode Verification for HODs.
     */
    @PostMapping("/otp/hod")
    public ResponseEntity<String> verifyHODOTP(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");

        System.out.println("Received email: " + email);
        System.out.println("Received OTP: " + otp);

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email or OTP is missing.");
        }

        boolean verified = authenticationService.verifyHODOTP(email, otp);

        return verified ? ResponseEntity.ok("HOD verification successful.")
                : ResponseEntity.badRequest().body("OTP verification failed.");

    }

    /**
     * Handles OTPCode Verification for Admins.
     */
    @PostMapping("/otp/admin")
    public ResponseEntity<String> verifyAdminOTP(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");

        System.out.println("Received email: " + email);
        System.out.println("Received OTP: " + otp);

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email or OTP is missing.");
        }

        boolean verified = authenticationService.verifyAdminOTP(email, otp);

        return verified ? ResponseEntity.ok("Admin verification successful.")
                : ResponseEntity.badRequest().body("OTP verification failed.");

    }

    @Autowired
    private final PasswordService passwordService;
    /**
     * Initiating forgot password sequence
     * Step 1: User Requests OTP for Password Reset.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return passwordService.createOtpForUser(request.getEmail());
    }

    /**
     * Step 2: User Resets Password Using OTP.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful.");
    }

}
