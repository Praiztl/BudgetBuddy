package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Exceptions.AuthenticationException;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.OTPCode;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import com.example.BudgetBuddy.Repositories.OTPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final HODRepository hodRepository;
    private final DepartmentRepository departmentRepository;
    private final OTPRepository otpRepository;
    private final DTOMapperService dtoMapperService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

    /**
     * Registers a new Admin.
     */
    public ResponseEntity<Map<String, String>> registerAdmin(AdminRegistrationDTO adminDTO) {
        // Check if email already exists in either Admin or HOD table
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent() ||
                hodRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "User with this email already exists"));
        }

        Admin newAdmin = dtoMapperService.convertToAdminEntity(adminDTO);
        newAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // Encode password
        Admin savedAdmin = adminRepository.save(newAdmin);

        // Generate and send OTP
        generateAndSendOTP(savedAdmin.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Admin registered successfully. OTP sent to email."));
    }

    /**
     * Registers a new HOD.
     */
    public ResponseEntity<Map<String, String>> registerHOD(HODRegistrationDTO dto) {
        // Check if email already exists in either HOD or Admin table
        if (hodRepository.findByEmail(dto.getEmail()).isPresent() ||
                adminRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "User with this email already exists"));
        }

        // Convert DTO to Entity
        HOD hod = dtoMapperService.convertToHODEntity(dto);
        hod.setPassword(passwordEncoder.encode(dto.getPassword())); // Encode password

        // Assign department and role
        hod.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));
        hod.setRole(HOD.Role.HOD);

        // Save HOD to repository
        HOD savedHOD = hodRepository.save(hod);

        // Generate and send OTP
        generateAndSendOTP(savedHOD.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "HOD registered successfully. OTP sent to email."));
    }


    /**
     * Handles user login (for both Admins & HODs).
     */
    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();

        // Check if user exists and is verified
        Optional<HOD> hodOptional = hodRepository.findByEmail(loginRequest.getEmail());
        Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());

        boolean isVerified = false;
        boolean isAdmin = false;


        if (hodOptional.isPresent()) {
            HOD hod = hodOptional.get();
            isVerified = hod.getIsOtpVerified();
        } else if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            isVerified = admin.getIsOtpVerified();
            isAdmin = true;
        }

        if (!isVerified) {
            response.put("error", "Account is not verified. Please complete OTP verification.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Extract user roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Generate JWT token
            String jwtToken = jwtService.generateToken(userDetails);

            // Populate response
            response.put("token", jwtToken);
            response.put("roles", roles);
            response.put("isAdmin", isAdmin);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

    /**
     * Checks if a given token belongs to an Admin.
     */
    public boolean isAdmin(String token) {
        String email = jwtService.extractUsername(token);
        return adminRepository.findByEmail(email).isPresent();
    }

    /**
     * Deletes an Admin by ID.
     */
    public void deleteAdmin(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Admin not found with id: " + id);
        }
    }

    /**
     * Deletes an HOD by ID.
     */
    public void deleteHOD(String id) {
        if (hodRepository.existsById(id)) {
            hodRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("HOD not found with id: " + id);
        }
    }

    /**
     * Verifies OTP for registration and clears OTP after successful verification.
     */
    public boolean verifyOTP(String email, String otp) {
        OTPCode otpCode = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new IllegalStateException("Invalid OTP or email"));

        if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            throw new IllegalStateException("OTP expired");
        }

        // Try to find user in HOD table first
        Optional<HOD> hod = hodRepository.findByEmail(email);
        if (hod.isPresent()) {
            hod.get().setIsOtpVerified(true);
            hodRepository.save(hod.get());

            // Clear OTP after successful verification
            clearOTP(email);

            return true;
        }

        // If not found in HOD, check Admin table
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            admin.get().setIsOtpVerified(true);
            adminRepository.save(admin.get());

            // Clear OTP after successful verification
            clearOTP(email);

            return true;
        }

        // If not found in either table, throw an error
        throw new IllegalStateException("User not found");
    }

    /**
     * Clears all OTPs tied to a given email after successful verification.
     */
    @Transactional // Ensures delete operation runs within a transaction
    public void clearOTP(String email) {
        otpRepository.deleteByEmail(email);
    }

    /**
     * Generates a 6-digit OTP, saves it in the database, and sends it via email.
     */
    private void generateAndSendOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(1000000)); // 6-digit OTP
        OTPCode otpCode = new OTPCode();
        otpCode.setEmail(email);
        otpCode.setOtp(otp);
        otpCode.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        otpRepository.save(otpCode);

        // Send OTP via email
        String subject = "Your OTP Code for BudgetBuddy";
        String body = "<p>Dear User,</p>"
                + "<p>Your OTP for verification is: <strong>" + otp + "</strong></p>"
                + "<p>This OTP will expire in 10 minutes.</p>"
                + "<p>Regards,<br>BudgetBuddy Team</p>";

        emailService.sendOtpEmail(email, otp);
    }
}
