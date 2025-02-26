package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Exceptions.InvalidOtpException;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.OTPCode;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import com.example.BudgetBuddy.Repositories.OTPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final AdminRepository adminRepository;
    private final HODRepository hodRepository;
    private final OTPRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${otp.expiration.time}") // Configured in application.properties
    private long otpExpirationTimeInMinutes;

    /**
     * Generate a random 6-digit OTP.
     */
    private String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000); // Ensures a 6-digit OTP
        return String.valueOf(otp);
    }

    /**
     * Step 1: Request OTP for Password Reset.
     */
    public ResponseEntity<?> createOtpForUser(String email) {
        // Check if email exists in Admin or HOD repository
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        HOD hod = hodRepository.findByEmail(email).orElse(null);

        if (admin == null && hod == null) {
            throw new UserNotFoundException("No Admin or HOD found with email: " + email);
        }

        // Generate OTP
        String otp = generateOtp();

        // Delete any existing OTP for this email
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);

        // Save new OTP in database
        OTPCode otpCode = new OTPCode();
        otpCode.setEmail(email);
        otpCode.setOtp(passwordEncoder.encode(otp)); // Store securely
        otpCode.setExpiryTime(LocalDateTime.now().plusMinutes(otpExpirationTimeInMinutes));
        otpRepository.save(otpCode);

        // Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return ResponseEntity.status(HttpStatus.OK).body("OTP has been sent to your email.");
    }

    /**
     * Step 2: Verify OTP before allowing password reset.
     */
    public boolean verifyOtp(String email, String otp) {
        OTPCode otpCode = otpRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidOtpException("OTP not found for email: " + email));

        if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, otpCode.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        return true; // OTP is valid
    }

    /**
     * Step 3: Reset Password after OTP verification.
     */
    public void resetPassword(String email, String otp, String newPassword) {
        // Verify OTP before proceeding
        verifyOtp(email, otp);

        // Find the user in either Admin or HOD repository
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        HOD hod = hodRepository.findByEmail(email).orElse(null);

        if (admin == null && hod == null) {
            throw new UserNotFoundException("No Admin or HOD found with email: " + email);
        }

        // Update password
        String encodedPassword = passwordEncoder.encode(newPassword);
        if (admin != null) {
            admin.setPassword(encodedPassword);
            adminRepository.save(admin);
        } else {
            hod.setPassword(encodedPassword);
            hodRepository.save(hod);
        }

        // Clear OTP after successful reset
        clearOtp(email);
    }

    /**
     * Remove OTP from the database after successful verification or expiration.
     */
    public void clearOtp(String email) {
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);
    }
}
