package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import com.example.BudgetBuddy.Repositories.OneTimeExpenseRepository;
import com.example.BudgetBuddy.Repositories.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    private DTOMapperService dtoMapperService;

    @Autowired
    private BudgetRepository budgetRepo;

    @Autowired
    private RecurringExpenseRepository recurringExpenseRepository;

    @Autowired
    private OneTimeExpenseRepository oneTimeExpenseRepository;

    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return ResponseEntity.ok(departments);
    }

    public Department createDepartment(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }

        Department department = new Department();
        department.setName(departmentName);
        return departmentRepository.save(department);
    }

    public ResponseEntity<Department> getDepartmentById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Updates a department name (Admin Only)
     */
    @Transactional
    public ResponseEntity<?> updateDepartment(Long departmentId, String newName) {
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admins can update departments"));
        }

        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        department.get().setName(newName);
        departmentRepository.save(department.get());

        return ResponseEntity.ok(Map.of("message", "Department updated successfully"));
    }

    public ResponseEntity<?> deleteDepartment(Long id) {
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admins can delete departments"));
        }

        Optional<Department> department = departmentRepository.findById(id);
        if (department.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        departmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }

    /**
     * Checks if the current user is an Admin based on Spring Security roles.
     */
    private boolean isCurrentUserAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    public ResponseEntity<Department> deleteDepartment(Long id){
        departmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<Budget> getApprovedBudgets(Long departmentId){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Approved) && Objects.equals(budget.getDepartment().getId(), departmentId)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<Budget> getPendingBudgets(Long departmentId){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Pending) && Objects.equals(budget.getDepartment().getId(), departmentId)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<Budget> getRejectedBudgets(Long departmentId){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Rejected) && Objects.equals(budget.getDepartment().getId(), departmentId)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<RecurringExpense> getRecurringExpenses(Long departmentId){
        List<RecurringExpense> expenses = recurringExpenseRepository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        for(RecurringExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
                response.add(expense);
            }
        }
        return response;
    }

    public List<OneTimeExpense> getOneTimeExpenses (Long departmentId){
        List<OneTimeExpense> expenses = oneTimeExpenseRepository.findAll();
        List<OneTimeExpense> response = new ArrayList<>();
        for(OneTimeExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
                response.add(expense);
            }
        }
        return response;
    }

    public List<Budget> getAllBudgets (Long departmentId){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(Objects.equals(budget.getDepartment().getId(), departmentId)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<BudgetDTO> getBudgetDTOs(Long departmentId){
        List<Budget> budgets = budgetRepo.findAll();
        List<BudgetDTO> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(Objects.equals(budget.getDepartment().getId(), departmentId)){
                response.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }
        return response;
    }

}
