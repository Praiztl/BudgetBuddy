package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.DepartmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private CsvUtil csvUtil;

    /**
     * Get all departments (Accessible by Anyone)
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * Create one or more departments (Only Admin)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createDepartments(@RequestBody DepartmentDTO departmentsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Only admins can create departments.");
        }

        if (departmentsDTO.getDepartments() == null || departmentsDTO.getDepartments().isEmpty()) {
            return ResponseEntity.badRequest().body("Departments list cannot be empty.");
        }

        List<Department> createdDepartments = new ArrayList<>();

        for (String department : departmentsDTO.getDepartments()) {
            Department savedDepartment = departmentService.createDepartment(department);
            createdDepartments.add(savedDepartment);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartments);
    }

    /**
     * Get department by ID (Accessible by Anyone)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    /**
     * Delete a department (Only Admin)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        // Get authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract user roles
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Only admins can delete departments.");
        }

//    @PutMapping(path = "/{id}/update")
//    private Department updateDepartment(@PathVariable(name = "id")Integer id, DepartmentDTO updates){
//        return service.updateDepartment(id, updates);
//    }
//
    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Department> deleteDepartment(@PathVariable(name = "id")Long id){
        return departmentService.deleteDepartment(id);
    }


    /*
    Endpoint for creating a budget within a department
     */
    @PostMapping(path = "/{id}/budgets/upload")
    public ResponseEntity<List<Budget>> read(@PathVariable(name = "id") Long id, @RequestBody MultipartFile file){

        if (csvUtil.hasCSVFormat(file)){
            try{
                return csvUtil.readBudget(file.getInputStream(), id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /*
    Endpoint for getting budgets for the department
     */
    @PostMapping(path = "/{id}/budgets")
    public ResponseEntity<List<Budget>> getBudgetsForDepartment(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(departmentService.getAllBudgets(id), HttpStatus.OK);
    }



    /*
        Department Dashboard endpoint
     */
    @GetMapping(path = "/{id}/dashboard")
    public ResponseEntity<HODDashboardCounts> getDashboard(@PathVariable(name = "id")Long id){
        HODDashboardCounts dashboard = new HODDashboardCounts(
                budgetService.getAllBudgets().size(),
                departmentService.getApprovedBudgets(id).size(),
                departmentService.getPendingBudgets(id).size(),
                departmentService.getRejectedBudgets(id).size(),
                recurringExpenseService.getRecurringExpenses().size(),
                ExpenseSummariser.currentMonthSummary(departmentService.getRecurringExpenses(id)),
                new HODExpenseChart(
                        ExpenseSummariser.getMonthlySummary(departmentService.getRecurringExpenses(id)),
                        ExpenseSummariser.getYearlySummary(departmentService.getRecurringExpenses(id))
                ),
                departmentService.getBudgetDTOs(id)
        );

        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

}
