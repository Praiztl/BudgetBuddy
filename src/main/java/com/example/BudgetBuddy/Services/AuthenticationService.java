package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
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
import java.util.Optional;
import java.util.Random;

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
    private final EmailService emailService; // Ensure EmailService is injected

    /**
     * Registers a new Admin.
     */
    public ResponseEntity<AdminResponseDTO> registerAdmin(AdminRegistrationDTO adminDTO) {
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Admin already exists
        }

        Admin newAdmin = dtoMapperService.convertToAdminEntity(adminDTO);
        newAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // Encode password
        Admin savedAdmin = adminRepository.save(newAdmin);

        // Generate and send OTP
        generateAndSendOTP(savedAdmin.getEmail());
        AdminResponseDTO responseDTO = dtoMapperService.convertToAdminResponseDTO(savedAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Registers a new HOD.
     */
    public ResponseEntity<HODResponseDTO> registerHOD(HODRegistrationDTO dto) {
        // Check if the HOD already exists
        if (hodRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // HOD already exists
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

        // Convert entity to response DTO
        HODResponseDTO responseDTO = dtoMapperService.convertToHODResponseDTO(savedHOD);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Handles user login (for both Admins & HODs).
     */
    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       /*     // Fetch the user from the repository
            HOD hod = hodRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            // Restrict login if the user is not verified or inactive
            if (!hod.getIsVerified() || !hod.getIsActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Account is not verified or inactive. Please complete OTP verification or contact admin.");
            }*/
            String jwtToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(jwtToken);
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
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
     * Verifies OTP for HOD registration.
     */
    public boolean verifyHODOTP(String email, String otp) {
        OTPCode otpCode = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new IllegalStateException("Invalid OTP or email"));

        if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            throw new IllegalStateException("OTP expired");
        }

        HOD hod = hodRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("HOD not found"));

        hod.setIsOtpVerified(true);
        hodRepository.save(hod);

        return true;
    }

    /**
     * Verifies OTP for Admin registration.
     */
    public boolean verifyAdminOTP(String email, String otp) {
        OTPCode otpCode = otpRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new IllegalStateException("Invalid OTP or email"));

        if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            throw new IllegalStateException("OTP expired");
        }

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Admin not found"));

        admin.setIsOtpVerified(true);
        adminRepository.save(admin);

        return true;
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
