package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final HODRepository hodRepository;
    private final DepartmentRepository departmentRepository;
    private final DTOMapperService dtoMapperService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Registers a new Admin.
     */
    public ResponseEntity<AdminResponseDTO> registerAdmin(AdminRegistrationDTO adminDTO) {
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Admin already exists
        }

        Admin newAdmin = dtoMapperService.convertToAdminEntity(adminDTO);
        newAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // Encode password
        newAdmin.setEnabled(true);
        Admin savedAdmin = adminRepository.save(newAdmin);

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
        hod.setEnabled(true);

        // Save HOD to repository
        HOD savedHOD = hodRepository.save(hod);

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
}
