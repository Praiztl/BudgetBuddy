package com.example.BudgetBuddy.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        String subject = "Your OTP Code for BudgetBuddy";
        String body = "<p>Dear User,</p>"
                + "<p>Your OTP for verification is: <strong>" + otp + "</strong></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>Regards,<br>BudgetBuddy Team</p>";

        sendHtmlEmail(to, subject, body);
    }

    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set `true` to enable HTML formatting

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
