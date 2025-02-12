package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Exceptions.InvalidOtpException;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.User;
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

    private final UserRepository userRepository;
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

        String otp = generateOtp();
        user.setOtp(passwordEncoder.encode(otp)); // Save OTP as a hashed value
        user.setOtpCreationTime(LocalDateTime.now());
        user.setOtpExpirationTime(LocalDateTime.now().plusMinutes(otpExpirationTimeInMinutes));
        userRepository.save(user);

        // Send OTP to user's email
        emailService.sendOtpEmail(user.getEmail(), otp); // Implemented EmailService
        return ResponseEntity.status(HttpStatus.OK).body("OTP has been sent to your email.");

    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP expired");
        }

        if (!passwordEncoder.matches(otp, user.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }

        // OTP is valid
        return true;
    }

    public void resetPassword(String email, String otp, String newPassword) {
        verifyOtp(email, otp); // This will throw InvalidOtpException if OTP is invalid or expired

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword)); // Encode new password
        userRepository.save(user);
        clearOtp(user); // Optionally clear OTP after successful password reset
    }

    public void clearOtp(User user) {
        user.setOtp(null);
        user.setOtpCreationTime(null);
        user.setOtpExpirationTime(null);
        userRepository.save(user);
    }
}
