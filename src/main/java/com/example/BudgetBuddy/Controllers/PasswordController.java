package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.CreateOtpDTO;
import com.example.BudgetBuddy.DTO.ResetPasswordDTO;
import com.example.BudgetBuddy.DTO.VerifyOtpDTO;
import com.example.BudgetBuddy.Services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody CreateOtpDTO createOtpDTO) {
        return passwordService.createOtpForUser(createOtpDTO.getEmail());

    }


    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO verifyOtpDTO) {
        boolean isValid = passwordService.verifyOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP is valid.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP.");
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        passwordService.resetPassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getNewPassword()); // Throws exception if invalid or expired
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
