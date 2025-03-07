package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Repositories.BudgetRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DTOMapperService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private HODRepository hodRepository;

    @Autowired
    private BudgetRepository budgetRepository;

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
        return BudgetDTO.builder()
                .id(budget.getId())
                .name(budget.getName())
                .date(budget.getCreatedAt())
                .amount(budget.getAmount())
                .departmentName(budget.getDepartment().getName())
                .approvalStatus(budget.getStatus())
                .build();
    }

    String defaultBudgetName = "Staff Welfare";

    public OneTimeExpenseDTO convertToExpenseDTO(OneTimeExpense expense){
        String budgetName = expense.getBudgetName();
        if(budgetName == null){
            budgetName = "Staff Welfare";
        }
        return OneTimeExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .assignedTo(expense.getAssignedTo().getId())
                .budgetName(budgetName)
                .createdAt(expense.getCreatedAt())
                .approvalStatus("Approved")
                .build();
    }

    public RecExpenseDTO convertToRecExpenseDTO(RecurringExpense expense){
        String budgetName = expense.getBudget();
        if(budgetName == null){
            budgetName = "Staff Welfare";
        }
        return RecExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .assignedTo(expense.getAssignedTo().getName())
                .budgetName(budgetName)
                .expenseInterval(expense.getExpenseInterval())
                .approvalStatus(expense.getApprovalStatus())
                .build();
    }

    public ExpenseDTO convertToMockRecExpenseDTO(OneTimeExpense expense){
        String budgetName = expense.getBudgetName();
        if(budgetName == null){
            budgetName = "Staff Welfare";
        }
        return ExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .amount(expense.getAmount())
                .assignedTo(expense.getAssignedTo().getName())
                .budgetName(budgetName)
                .approvalStatus("Approved")
                .expenseInterval("one-time expense")
                .build();
    }

    public String getHODForDepartment(Long departmentId){
        List<HOD> allHods = hodRepository.findAll();
        for (HOD hod : allHods){
            if (Objects.equals(hod.getDepartment().getId(), departmentId)){
                return hod.getFirstName() + " " + hod.getLastName();
            }
        }
        return null;
    }

    public GetDepartmentDTO convertToGetDepartmentDTO(Department department){
        List<BudgetDTO> budgetDTOS = new ArrayList<>();
        List<RecExpenseDTO>recExpenseDTOS = new ArrayList<>();
        List<OneTimeExpenseDTO> oneTimeExpenseDTOS = new ArrayList<>();

        for(Budget budget: department.getBudgets()){
            budgetDTOS.add(convertToBudgetDTO(budget));
        }

        for (RecurringExpense expense: department.getRecurringExpenses()){
            recExpenseDTOS.add(convertToRecExpenseDTO(expense));
        }

        for (OneTimeExpense expense: department.getExpenses()){
            oneTimeExpenseDTOS.add(convertToExpenseDTO(expense));
        }
        return GetDepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .budgets(budgetDTOS)
                .hod(getHODForDepartment(department.getId()))
                .oneTimeExpenses(oneTimeExpenseDTOS)
                .recurringExpenses(recExpenseDTOS)
                .createdAt(department.getCreatedAt())
                .build();
    }

    public NotificationDTO convertToNotificationDTO(Notification notification){
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .departmentId(notification.getAssignedTo().getId())
                .from(notification.getFrom())
                .date(notification.getDate())
                .time(notification.getTime())
                .isRead(notification.getRead())
                .build();
    }
}
