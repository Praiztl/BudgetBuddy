package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.HODRegistrationDTO;
import com.example.BudgetBuddy.DTO.HODResponseDTO;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Repositories.HODRepository;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HODService {

    private final HODRepository hodRepository;
    private final DepartmentRepository departmentRepository;
    private final DTOMapperService dtoMapperService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<HODResponseDTO> registerHOD(HODRegistrationDTO hodRegistrationDTO) {
        // Check if department exists
        Department department = departmentRepository.findById(hodRegistrationDTO.getDepartmentId())
                .orElse(null);
        if (department == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Department not found
        }

        // Create the HOD user
        HOD hod = dtoMapperService.convertToHODEntity(hodRegistrationDTO);
        hod.setPassword(passwordEncoder.encode(hodRegistrationDTO.getPassword())); // Encode password
        hod.setDepartment(department); // Associate department
        hod.setRole(HOD.Role.HOD); // Ensure the role is set to "HOD"

        // Save the HOD to the database
        HOD createdHOD = hodRepository.save(hod);  // Correct repository usage

        // Return the response
        HODResponseDTO userResponseDTO = dtoMapperService.convertToHODResponseDTO(createdHOD); // Fix DTO mapping for HOD
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    public ResponseEntity<HOD> findByEmail(String email){
        Optional<HOD> hod = hodRepository.findByEmail(email);
        HOD userHOD = null;

        if(hod.isEmpty()){
            List<HOD> hods = hodRepository.findAll();
            for (HOD hodObject: hods){
                if(hodObject.getEmail().equals(email)){
                    userHOD = hodObject;
                    break;
                }
            }
        }
        return new ResponseEntity<>(userHOD, HttpStatus.FOUND);
    }

/*
Endpoints to access budgets relevant to that HOD
 */
}
