package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Exceptions.InvalidOtpException;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.OTPCode;
import com.example.BudgetBuddy.Models.User;
import com.example.BudgetBuddy.Repositories.OTPRepository;
import com.example.BudgetBuddy.Repositories.UserRepository;
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

    private final UserRepository<User> userRepository;
    private final OTPRepository otpRepository;  // Updated to use OTPRepository
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${otp.expiration.time}") // Configured in application.properties
    private long otpExpirationTimeInMinutes;

    public String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    public ResponseEntity<?> createOtpForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Generate OTP
        String otp = generateOtp();

        // Delete any existing OTP for the email before saving a new one
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);

        // Save OTP in the database
        OTPCode otpCode = new OTPCode();
        otpCode.setEmail(email);
        otpCode.setOtp(passwordEncoder.encode(otp)); // Save OTP securely
        otpCode.setExpiryTime(LocalDateTime.now().plusMinutes(otpExpirationTimeInMinutes));
        otpRepository.save(otpCode);

        // Send OTP via email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return ResponseEntity.status(HttpStatus.OK).body("OTP has been sent to your email.");
    }

    public boolean verifyOtp(String email, String otp) {
        OTPCode otpCode = otpRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidOtpException("OTP not found for email: " + email));

        if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP expired");
        }

        // Validate OTP
        if (!passwordEncoder.matches(otp, otpCode.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        return true;  // OTP is valid
    }

    public void resetPassword(String email, String otp, String newPassword) {
        // Verify OTP before resetting password
        verifyOtp(email, otp);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword)); // Encode new password
        userRepository.save(user);

        clearOtp(email);  // Delete OTP after successful password reset
    }

    public void clearOtp(String email) {
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete); // Remove OTP after use
    }
}
