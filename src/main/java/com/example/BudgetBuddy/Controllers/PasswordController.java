package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.OTPCodeDTO;
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

    /**
     * Generate and send OTP to the user's email.
     */
    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@RequestBody OTPCodeDTO otpCodeDTO) {
        return passwordService.createOtpForUser(otpCodeDTO.getEmail());
    }

    /**
     * Verify OTP for a user.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO verifyOtpDTO) {
        passwordService.verifyOtp(verifyOtpDTO.getEmail(), verifyOtpDTO.getOtp());
        return ResponseEntity.ok("OTP is valid.");
    }

    /**
     * Reset user password after OTP verification.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        passwordService.resetPassword(resetPasswordDTO.getEmail(), resetPasswordDTO.getOtp(), resetPasswordDTO.getNewPassword());
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
