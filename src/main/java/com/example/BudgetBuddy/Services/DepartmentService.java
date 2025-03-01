package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.BudgetRepository;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import com.example.BudgetBuddy.Repositories.OneTimeExpenseRepository;
import com.example.BudgetBuddy.Repositories.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public ResponseEntity<Department> createDepartment(Department department) {
//        Department department = new Department(departmentName);
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }

    public ResponseEntity<Department> getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(department);
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
