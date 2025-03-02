package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.BudgetRepository;
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

import java.time.LocalDate;
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

    public Double getMonthlyAmountRecurringExpenses(Long departmentId, String monthName){
        List<RecurringExpense> expenses = recurringExpenseRepository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(RecurringExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId) && expense.getCreatedAt().getYear() == LocalDate.now().getYear()){
                response.add(expense);
            }
        }
        for(RecurringExpense expense : response){
            if(expense.getCreatedAt().getMonth().name().substring(0,3).equals(monthName)){
                if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                    total+=(expense.getAmount()*4);
                }
                else if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                    total += expense.getAmount()*(expense.getCreatedAt().getMonth().length(expense.getCreatedAt().isLeapYear()));
                }else{
                    total += expense.getAmount();
                }
            }
        }
        return total;
    }

    public Double getYearlyAmountRecurringExpenses(Long departmentId, Integer year){
        List<RecurringExpense> expenses = recurringExpenseRepository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(RecurringExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
                response.add(expense);
            }
        }
        for(RecurringExpense expense : response){
            if(expense.getCreatedAt().getYear()==year){
                if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                    total+=(expense.getAmount()*52);
                }
                else if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                    if(expense.getCreatedAt().isLeapYear()){
                        total += expense.getAmount()*366;
                    }else{
                        total += expense.getAmount()*365;
                    }
                } else if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Monthly)) {
                    total += expense.getAmount()*12;
                }else {
                    total += expense.getAmount();
                }
            }
        }
        return total;
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

    public Double getMonthlyAmountOneTimeExpenses(Long departmentId, String monthName){
        List<OneTimeExpense> expenses = oneTimeExpenseRepository.findAll();
        List<OneTimeExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(OneTimeExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId) && expense.getCreatedAt().getYear()==LocalDate.now().getYear()){
                response.add(expense);
            }
        }
        for(OneTimeExpense expense : response){
            if(expense.getCreatedAt().getMonth().name().substring(0,3).equals(monthName)){
                total += expense.getAmount();
            }
        }
        return total;
    }

    public Double getYearlyAmountOneTimeExpenses(Long departmentId, Integer year){
        List<OneTimeExpense> expenses = oneTimeExpenseRepository.findAll();
        List<OneTimeExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(OneTimeExpense expense : expenses){
            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
                response.add(expense);
            }
        }
        for(OneTimeExpense expense : response){
            if(expense.getCreatedAt().getYear()==year){
                total += expense.getAmount();
            }
        }
        return total;
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
