package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.*;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.*;
import lombok.RequiredArgsConstructor;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

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
    public ResponseEntity<List<GetDepartmentDTO>> getAllDepartments(@RequestParam(name = "organizationName", required = false) String orgName) {
        List<Department> departments = new ArrayList<>();
        List<GetDepartmentDTO> response = new ArrayList<>();
        if(orgName==null){
            departments = departmentService.getAllDepartments().getBody();
        }
        else{
            departments = departmentService.getDepartmentsByNameForOrganization(orgName);
        }
        for(Department department: departments){
            response.add(dtoMapperService.convertToGetDepartmentDTO(department));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create one or more departments (Only Admin)
     */
    @PostMapping("/create/{orgId}")
    public ResponseEntity<?> createDepartments(@PathVariable("orgId") Long orgId, @RequestBody DepartmentDTO departmentsDTO) {
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
            Department savedDepartment = departmentService.createDepartment(department, orgId);
            createdDepartments.add(savedDepartment);
        }

        List<GetDepartmentDTO> departmentDTOs = new ArrayList<>();
        for(Department department: createdDepartments){
            departmentDTOs.add(dtoMapperService.convertToGetDepartmentDTO(department));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartments);
    }

    /**
     * Get department by ID (Accessible by Anyone)
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetDepartmentDTO> getDepartmentById(@PathVariable Long id) {
        return new ResponseEntity<>(dtoMapperService.convertToGetDepartmentDTO(departmentService.getDepartmentById(id).getBody()), HttpStatus.OK);
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
    @PostMapping(path = "/{id}/budgets/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<BudgetDTO>> read(@PathVariable(name = "id") Long id, @RequestParam("files") List<MultipartFile> files) {
        List<BudgetDTO> allBudgets = new ArrayList<>();

        for (MultipartFile file : files) {
            if (csvUtil.hasCSVFormat(file)) {
                try {
                    List<BudgetDTO> budgets = new ArrayList<>();
                    for (Budget budget : csvUtil.readBudget(file.getInputStream(), id).getBody()) {
                        budgets.add(dtoMapperService.convertToBudgetDTO(budget));
                    }
                    allBudgets.addAll(budgets);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new ResponseEntity<>(allBudgets, HttpStatus.CREATED);
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
    Endpoints for one time expenses for the department
     */
    @PostMapping(path = "/{id}/expenses/create")
    public OneTimeExpenseDTO createOneTimeExpense(@PathVariable(name = "id") Long id, @RequestBody OneTimeExpense expense){
        return dtoMapperService.convertToExpenseDTO(oneTimeExpenseService.createOneTimeExpense(expense, id).getBody());
    }


    @GetMapping(path = "/{id}/expenses")
    public List<OneTimeExpenseDTO> getExpensesForBudget(@PathVariable(name = "id") Long id){
        List<OneTimeExpense> expenses = oneTimeExpenseService.getForDepartment(id).getBody();
        List<OneTimeExpenseDTO> response = new ArrayList<>();
        for (OneTimeExpense expense: expenses){
            response.add(dtoMapperService.convertToExpenseDTO(expense));
        }
        return response;
    }


        /*
    Endpoints for Recurring Expenses
     */

    @PostMapping(path = "/{id}/recurringexpenses/create")
    public RecExpenseDTO createRecurringExpense(@PathVariable(name = "id") Long departmentId, @RequestBody RecurringExpense expense){
        RecurringExpense expenses = recurringExpenseService.createRecurringExpense(expense, departmentId);
        return dtoMapperService.convertToRecExpenseDTO(expenses);
    }

    @GetMapping(path = "/{id}/recurringexpenses")
    public List<RecExpenseDTO> getRecExpenses(@PathVariable(name = "id") Long departmentId){
        List<RecurringExpense> result =  recurringExpenseService.getRecurringExpenses();
        List<RecExpenseDTO> response = new ArrayList<>();

        for (RecurringExpense expense : result){
            if (expense.getAssignedTo().getId().equals(departmentId)){
                response.add(dtoMapperService.convertToRecExpenseDTO(expense));
            }
        }
        return response;
    }


    /*
    Getting notifications for this department
     */
    @GetMapping(path = "/{id}/notifications")
    public ResponseEntity<?> getNotificationsForHOD(@PathVariable(name = "id") Long id){
        Map<String, Object> notificationMapping = new HashMap<>();
        List<NotificationDTO> notifications = notificationService.getNotificationsByDepartment(departmentService.getDepartmentById(id).getBody());
        List<NotificationDTO> recent = new ArrayList<>();
        List<NotificationDTO> older = new ArrayList<>();

        for (NotificationDTO notification: notifications){
            if(LocalDate.now().compareTo(notification.getDate())<3){
                recent.add(notification);
            }
            else{
                older.add(notification);
            }
        }
        notificationMapping.put("recent", recent);
        notificationMapping.put("older", older);

        return new ResponseEntity<>(notificationMapping, HttpStatus.OK);
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

    @GetMapping(path = "/{id}/dashboard/pending-recurring-expense-count")
    public ResponseEntity<Integer> getPendingRecurringExpenseCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getPendingRecurringExpenses(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/approved-recurring-expense-count")
    public ResponseEntity<Integer> getApprovedRecurringExpenseCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getApprovedRecurringExpenses(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/rejected-recurring-expense-count")
    public ResponseEntity<Integer> getRejectedRecurringExpenseCount(@PathVariable(name = "id") Long departmentId){
        Integer response = departmentService.getRejectedRecurringExpenses(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping(path = "/{id}/dashboard/expense-chart")
    public ResponseEntity<HODExpenseChart> getHODExpenseChart(@PathVariable(name = "id") Long departmentId){
        List<Expense> expenseList = new ArrayList<>();
        for(RecurringExpense expense :departmentService.getApprovedRecurringExpenses(departmentId)){
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
        Map<String, Object> response =  expenseSummariser.currentMonthSummary(departmentService.getApprovedRecurringExpenses(departmentId),monthValue);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/budget-list")
    public ResponseEntity<List<BudgetDTO>> getBudgetList(@PathVariable(name = "id")Long departmentId){
        List<BudgetDTO> response = departmentService.getBudgetDTOs(departmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/dashboard/recurring-expense-list")
    public ResponseEntity<?> getRecExpenseList(@PathVariable(name = "id") Long departmentId){
        List<RecurringExpense> resultRecurring = departmentService.getRecurringExpenses(departmentId);
        List<OneTimeExpense> resultOneTime = departmentService.getOneTimeExpenses(departmentId);

        List<Object> response = new ArrayList<>();
        for(RecurringExpense expense:resultRecurring){
            response.add(dtoMapperService.convertToRecExpenseDTO(expense));
        }

        for(OneTimeExpense expense: resultOneTime){
            response.add(dtoMapperService.convertToMockRecExpenseDTO(expense));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
