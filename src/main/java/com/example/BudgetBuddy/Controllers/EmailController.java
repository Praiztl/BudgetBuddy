package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        emailService.sendOtpEmail(email, "123456"); // Use a generated OTP in real cases
        return ResponseEntity.ok("OTP email sent to " + email);
    }
}
