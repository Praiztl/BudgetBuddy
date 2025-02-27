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
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegistrationDTO adminDTO) {
        return authenticationService.registerAdmin(adminDTO);
    }
    /**
     * Registers a new HOD.
     */
    @PostMapping("/signup/hod")
    public ResponseEntity<?> registerHOD(@RequestBody HODRegistrationDTO dto) {
        return authenticationService.registerHOD(dto);
    }
    /**
     * Handles user login for both Admins & HODs.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    /**
     * Handles OTPCode Verification for registration.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String otp = requestBody.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email or OTP is missing.");
        }

        boolean verified = authenticationService.verifyOTP(email, otp);

        if (verified) {
            authenticationService.clearOTP(email); // Clear OTP after successful verification
            return ResponseEntity.ok("Verification successful.");
        } else {
            return ResponseEntity.badRequest().body("OTP verification failed.");
        }
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
