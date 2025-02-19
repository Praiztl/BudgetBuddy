package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.AdminRegistrationDTO;
import com.example.BudgetBuddy.DTO.AdminResponseDTO;
import com.example.BudgetBuddy.DTO.HODRegistrationDTO;
import com.example.BudgetBuddy.DTO.HODResponseDTO;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final AdminRepository adminRepository;
    private final HODRepository hodRepository;
    private final DTOMapperService dtoMapperService;

    public ResponseEntity<HODResponseDTO> createHOD(HODRegistrationDTO hodRegistrationDTO) {
        // Check if the HOD already exists
        if (hodRepository.findByEmail(hodRegistrationDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        HOD hod = dtoMapperService.convertToHODEntity(hodRegistrationDTO);
        hodRepository.save(hod); // Save HOD using the HOD repository

        HODResponseDTO userResponseDTO = dtoMapperService.convertToHODResponseDTO(hod);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    public ResponseEntity<AdminResponseDTO> createAdmin(AdminRegistrationDTO adminRegistrationDTO) {
        // Check if the Admin already exists
        if (adminRepository.findByEmail(adminRegistrationDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Admin admin = dtoMapperService.convertToAdminEntity(adminRegistrationDTO);
        adminRepository.save(admin); // Save Admin using the Admin repository

        AdminResponseDTO adminResponseDTO = dtoMapperService.convertToAdminResponseDTO(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminResponseDTO);
    }

    public void deleteUser(String id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        } else if (hodRepository.existsById(id)) {
            hodRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }




}
