package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.AdminRegistrationDTO;
import com.example.BudgetBuddy.DTO.AdminResponseDTO;
import com.example.BudgetBuddy.DTO.HODRegistrationDTO;
import com.example.BudgetBuddy.DTO.HODResponseDTO;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DTOMapperService {

    private final PasswordEncoder passwordEncoder;



    // Convert HOD entity to HODResponseDTO (specific for HOD)
    public HODResponseDTO convertToHODResponseDTO(HOD hod) {
        return HODResponseDTO.builder()
                .firstName(hod.getFirstName())  // Assuming HOD has firstName
                .lastName(hod.getLastName())    // Assuming HOD has lastName
                .email(hod.getEmail())
                .role(User.Role.HOD)  // Assuming HOD role is HOD
                .build();
    }

    // Convert Admin entity to AdminResponseDTO (specific for Admin)
    public AdminResponseDTO convertToAdminResponseDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .email(admin.getEmail())
                .role(User.Role.ADMIN)  // Assuming Admin inherits role from User
                .build();
    }

    // Convert AdminRegistrationDTO to Admin entity
    public Admin convertToAdminEntity(AdminRegistrationDTO adminRegistrationDTO) {
        Admin admin = new Admin();
        admin.setEmail(adminRegistrationDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminRegistrationDTO.getPassword())); // Encode password
        admin.setRole(Admin.Role.ADMIN); // Set role as Admin
        return admin;
    }

    // Convert HODRegistrationDTO to HOD entity
    public HOD convertToHODEntity(HODRegistrationDTO hodRegistrationDTO) {
        HOD hod = new HOD();
        hod.setFirstName(hodRegistrationDTO.getFirstName());
        hod.setLastName(hodRegistrationDTO.getLastName());
        hod.setEmail(hodRegistrationDTO.getEmail());
        hod.setPassword(passwordEncoder.encode(hodRegistrationDTO.getPassword())); // Encode password
        hod.setRole(HOD.Role.HOD); // Set role as HOD
        hod.setDepartment(hodRegistrationDTO.getDepartment()); // Set department for HOD
        return hod;
    }
}
