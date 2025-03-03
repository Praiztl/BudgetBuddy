package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.AdminRegistrationDTO;
import com.example.BudgetBuddy.DTO.AdminResponseDTO;
import com.example.BudgetBuddy.DTO.HODResponseDTO;
import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;  // Use AdminRepository directly
    private final DTOMapperService dtoMapperService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<AdminResponseDTO> registerAdmin(AdminRegistrationDTO adminRegistrationDTO) {
        // Create Admin user
        Admin admin = dtoMapperService.convertToAdminEntity(adminRegistrationDTO);
        admin.setPassword(passwordEncoder.encode(adminRegistrationDTO.getPassword())); // Encode password
        admin.setRole(Admin.Role.ADMIN); // Set admin role
        Admin createdAdmin = adminRepository.save(admin);

        // Return the response
        AdminResponseDTO adminResponseDTO = dtoMapperService.convertToAdminResponseDTO(createdAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminResponseDTO);
    }

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    public Map<String, Integer> getBudgetStatistics(){
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("budgets", budgetService.getAllBudgets().size());
        statistics.put("recExpenses", recurringExpenseService.getRecurringExpenses().size());
        statistics.put("departments", departmentService.getAllDepartments().getBody().size());

        return statistics;
    }

}
