package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DTOMapperService {

    private final PasswordEncoder passwordEncoder;



    // Convert HOD entity to HODResponseDTO (specific for HOD)
    public HODResponseDTO convertToHODResponseDTO(HOD hod) {
        return HODResponseDTO.builder()
                .firstName(hod.getFirstName())
                .lastName(hod.getLastName())
                .email(hod.getEmail())
                .role(User.Role.HOD)
                .build();
    }

    // Convert Admin entity to AdminResponseDTO (specific for Admin)
    public AdminResponseDTO convertToAdminResponseDTO(Admin admin) {
        return AdminResponseDTO.builder()
                .email(admin.getEmail())
                .role(User.Role.ADMIN)
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

    public BudgetDTO convertToBudgetDTO(Budget budget){
        List<String> oneTimeExpenses = new ArrayList<>();
        for(OneTimeExpense expense: budget.getExpenses()){
            oneTimeExpenses.add(expense.toString());
        }

        List<String> recurringExpenses = new ArrayList<>();
        for(RecurringExpense expense: budget.getRecurringExpenses()){
            recurringExpenses.add(expense.toString());
        }

        return BudgetDTO.builder()
                .id(budget.getId())
                .name(budget.getName())
                .date(budget.getCreatedAt())
                .amount(budget.getAmount())
                .expenses(oneTimeExpenses)
                .recurringExpenses(recurringExpenses)
                .approvalStatus(budget.getStatus())
                .build();
    }

    public OneTimeExpenseDTO convertToExpenseDTO(OneTimeExpense expense){
        return OneTimeExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .assignedTo(expense.getAssignedTo().getName())
                .createdAt(expense.getCreatedAt())
                .build();
    }

    public RecExpenseDTO convertToRecExpenseDTO(RecurringExpense expense){
        return RecExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .assignedTo(expense.getAssignedTo().getName())
                .expenseInterval(expense.getExpenseInterval())
                .approvalStatus(expense.getApprovalStatus())
                .build();
    }

    public GetDepartmentDTO convertToGetDepartmentDTO(Department department){
        List<BudgetDTO> budgetDTOS = new ArrayList<>();
        for(Budget budget: department.getBudgets()){
            budgetDTOS.add(convertToBudgetDTO(budget));
        }
        return GetDepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .budgets(budgetDTOS)
//                .hod(department.getHod().getId())
                .build();
    }
}
