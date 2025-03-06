package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.DTO.GetDepartmentDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.*;
import lombok.RequiredArgsConstructor;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private NotificationService notificationService;

    @Autowired
    private CsvUtil csvUtil;

    @Autowired
    private ExpenseSummariser expenseSummariser;

    @Autowired
    private DTOMapperService dtoMapperService;

    /**
     * Get all departments (Accessible by Anyone)
     */
    @GetMapping
    public ResponseEntity<List<GetDepartmentDTO>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments().getBody();
        List<GetDepartmentDTO> response = new ArrayList<>();
        for(Department department: departments){
            response.add(dtoMapperService.convertToGetDepartmentDTO(department));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create one or more departments (Only Admin)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createDepartments(@RequestBody DepartmentDTO departmentsDTO) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .anyMatch(role -> role.equals("ROLE_ADMIN"));
//
//        if (!isAdmin) {
//            return ResponseEntity.status(403).body("Access denied: Only admins can create departments.");
//        }

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
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
//        // Get authentication details
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Extract user roles
//        boolean isAdmin = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .anyMatch(role -> role.equals("ROLE_ADMIN"));
//
//        if (!isAdmin) {
//            return ResponseEntity.status(403).body("Access denied: Only admins can delete departments.");
//        }

        return departmentService.deleteDepartment(id);
    }



    /*
    Endpoint for creating a budget within a department
     */
    @PostMapping(path = "/{id}/budgets/upload")
    public ResponseEntity<List<BudgetDTO>> read(@PathVariable(name = "id") Long id, @RequestBody MultipartFile file){

        if (csvUtil.hasCSVFormat(file)){
            try{
                List<BudgetDTO> budgets = new ArrayList<>();
                for (Budget budget :  csvUtil.readBudget(file.getInputStream(), id).getBody()){
                    budgets.add(dtoMapperService.convertToBudgetDTO(budget));
                }
                return new ResponseEntity<>(budgets, HttpStatus.CREATED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /*
    Endpoint for getting budgets for the department
     */
    @GetMapping(path = "/{id}/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgetsForDepartment(@PathVariable(name = "id") Long id){
        List<BudgetDTO> budgets = new ArrayList<>();
        for (Budget budget : departmentService.getAllBudgets(id)){
            budgets.add(dtoMapperService.convertToBudgetDTO(budget));
        }
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    /*
    Getting notifications for this department
     */
    @GetMapping(path = "/{id}/notifications")
    public ResponseEntity<List<Notification>> getNotificationsForHOD(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(notificationService.getNotificationsByDepartment(departmentService.getDepartmentById(id).getBody().getName()), HttpStatus.OK);
    }



    /*
        Department Dashboard endpoint
    Find alternative dashboard endpoints in HOD controller
     */

    @GetMapping(path = "/{id}/dashboard/total-budget-count")
    public ResponseEntity<Integer> getBudgetCounts(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getAllBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/approved-budget-count")
    public ResponseEntity<Integer> getApprovedBudgetCount(@PathVariable(name = "id") Long departmentId) {
        Integer response = departmentService.getApprovedBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/pending-budget-count")
    public ResponseEntity<Integer>getPendingBudgetCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getPendingBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/rejected-budget-count")
    public ResponseEntity<Integer> getRejectedBudgetCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getRejectedBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/total-recurring-expense-count")
    public ResponseEntity<Integer> getTotalRecurringExpenseCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getRecurringExpenses(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/expense-chart")
    public ResponseEntity<HODExpenseChart> getHODExpenseChart(@PathVariable(name = "id") Long departmentId){
        List<Expense> expenseList = new ArrayList<>();
        for(RecurringExpense expense :departmentService.getRecurringExpenses(departmentId)){
            expenseList.add(expense);
        }
        for(OneTimeExpense expense :departmentService.getOneTimeExpenses(departmentId)){
            expenseList.add(expense);
        }

        HODExpenseChart expenseChart = new HODExpenseChart(
                expenseSummariser.getMonthlySummary(expenseList, departmentId),
                expenseSummariser.getYearlySummary(expenseList, departmentId)
        );
        return new ResponseEntity<>(expenseChart, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/expense-summary")
    public ResponseEntity <Map<String, Object>> getExpenseSummary(@PathVariable(name = "id") Long departmentId, @RequestParam(name = "month", required = false) String monthValue){
        Map<String, Object> response =  expenseSummariser.currentMonthSummary(departmentService.getRecurringExpenses(departmentId),monthValue);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/budget-list")
    public ResponseEntity<List<BudgetDTO>> getBudgetList(@PathVariable(name = "id")Long departmentId){
        List<BudgetDTO> response = departmentService.getBudgetDTOs(departmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
