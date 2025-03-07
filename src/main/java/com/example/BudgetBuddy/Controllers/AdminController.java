package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.DTO.NotificationDTO;
import com.example.BudgetBuddy.DTO.RecExpenseDTO;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.*;
import com.example.BudgetBuddy.Utilities.AdminExpenseCalculator;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @Autowired
    private ExpenseSummariser expenseSummariser;

    @Autowired
    private AdminExpenseCalculator adminExpenseCalculator;

    @Autowired
    private DTOMapperService dtoMapperService;

    @Autowired
    private AdminService adminService;

    @GetMapping(path = "/notifications")
    public ResponseEntity<?> getNotificationsForaAdmin(){
        Map<String, List<NotificationDTO>> notificationMapping = new HashMap<>();

        List<NotificationDTO> allNotifications = notificationService.getNotificationsForAdmin();
        List<NotificationDTO> older = new ArrayList<>();
        List<NotificationDTO> recent = new ArrayList<>();

        for(NotificationDTO notification: allNotifications){
            if(notification.getDate()!= null && LocalDate.now().compareTo(notification.getDate())<3){
                recent.add(notification);
            } else{
                older.add(notification);
            }
        }

        notificationMapping.put("recent", recent);
        notificationMapping.put("older", older);

        return new ResponseEntity<>(notificationMapping, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-budget-count-per-department")
    public ResponseEntity<Map<String, Integer>> getBudgetPerDepartment(){
        List<Department> departments = departmentService.getAllDepartments().getBody();
        Map<String, Integer> departmentCounts = new HashMap<>();
        assert departments != null;
        for(Department department: departments){
            Integer budgetCount = departmentService.getAllBudgets(department.getId()).size();
            departmentCounts.put(department.getName(), budgetCount);
        }

        return new ResponseEntity<>(departmentCounts, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-budget-statistics")
    public ResponseEntity<?> getBudgetStatistics(){         //Map<String, Integer>
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON ).body(adminService.getBudgetStatistics());
    }

    @GetMapping(path = "/dashboard/get-total-budget-count")
    public Integer getTotalBudgetCount(@RequestParam(name = "status", required = false)Budget.Status status){
        Integer count = 0;
        if(status==null){
            count = budgetService.getAllBudgets().size();
        }
        else if(status.equals(Budget.Status.Approved)){
            count = budgetService.getApprovedBudgets().size();
        } else if (status.equals(Budget.Status.Pending)){
            count = budgetService.getPendingBudgets().size();
        } else if (status.equals(Budget.Status.Rejected)) {
            count = budgetService.getRejectedBudgets().size();
        }

       return count;
    }

    @GetMapping(path = "/dashboard/get-recurring-expense-count")
    public Map<String, Integer> getRecurringExpenseCount(){
        List<Department> departments = departmentService.getAllDepartments().getBody();
        Map<String, Integer> departmentCounts = new HashMap<>();
        assert departments != null;
        for(Department department: departments){
            Integer recurringExpenseCount = departmentService.getRecurringExpenses(department.getId()).size();
            departmentCounts.put(department.getName(), recurringExpenseCount);
        }

        return departmentCounts;
    }

    @GetMapping(path = "/dashboard/get-total-recurring-expense-count")
    public Integer getTotalRecurringExpenseCount(@RequestParam(name = "status", required = false) RecurringExpense.Status status) {
        Integer count = 0;
        if (status == null) {
            count = recurringExpenseService.getRecurringExpenses().size();
        }
        else if (status.equals(RecurringExpense.Status.Approved)) {
            count = recurringExpenseService.getApprovedExpenses().size();
        } else if (status.equals(RecurringExpense.Status.Pending)) {
            count = recurringExpenseService.getPendingExpenses().size();
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            count = recurringExpenseService.getRejectedExpenses().size();
        }

        return count;
    }


    @GetMapping(path = "/dashboard/get-department-count")
    public Integer getDepartmentCount(){
        Integer departmentCount = departmentService.getAllDepartments().getBody().size();
        return departmentCount;
    }

    @GetMapping(path = "/dashboard/get-expense-chart")
    public HODExpenseChart getExpenseChart(){
        List<Expense> expenseList = new ArrayList<>();
        for(OneTimeExpense expense: oneTimeExpenseService.getOneTimeExpenses()){
            expenseList.add(expense);
        }
        for(RecurringExpense expense: recurringExpenseService.getRecurringExpenses()){
            expenseList.add(expense);
        }
        HODExpenseChart response = new HODExpenseChart(
                expenseSummariser.getAdminMonthlySummary(expenseList),
                expenseSummariser.getAdminYearlySummary(expenseList)
        );
        return response;
    }

    @GetMapping(path = "/dashboard/get-yearly-budget-total")
    public Map<Integer, Double> getYearlyBudgetTotal(){
        List<Budget> budgetList = budgetService.getApprovedBudgets();
        Map<Integer, Double> response = expenseSummariser.getAdminYearlyBudgetTotal(budgetList);
        return response;
    }

    @GetMapping(path = "/dashboard/get-total-budget-list")
    public List<BudgetDTO> getTotalBudgetList(@RequestParam(name = "status", required = false)Budget.Status status){
        List<BudgetDTO> totalBudgetDTOList = new ArrayList<>();
        List<Budget> totalBudgetList = new ArrayList<>();

        if(status == null) {
            totalBudgetList = budgetService.getAllBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }
        else if(status.equals(Budget.Status.Approved)){
            totalBudgetList = budgetService.getApprovedBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(Budget.Status.Pending)){
            totalBudgetList = budgetService.getPendingBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(Budget.Status.Rejected)) {
            totalBudgetList = budgetService.getRejectedBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }

        return totalBudgetDTOList;
    }

    @GetMapping(path = "/dashboard/get-total-recurring-expense-list")
    public List<Object> getTotalRecurringExpenseList(@RequestParam(name = "status", required = false) RecurringExpense.Status status){
        List<RecurringExpense> totalRecExpenseList = new ArrayList<>();
        List<OneTimeExpense> totalOneTimeExpenseList = new ArrayList<>();
        List<Object> response = new ArrayList<>();

        if(status == null) {
            totalRecExpenseList=recurringExpenseService.getRecurringExpenses();
            totalOneTimeExpenseList=oneTimeExpenseService.getOneTimeExpenses();
        }
        else if(status.equals(RecurringExpense.Status.Approved)){
            totalRecExpenseList=recurringExpenseService.getApprovedExpenses();
            totalOneTimeExpenseList=oneTimeExpenseService.getOneTimeExpenses();
        } else if (status.equals(RecurringExpense.Status.Pending)){
            totalRecExpenseList=recurringExpenseService.getPendingExpenses();
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            totalRecExpenseList=recurringExpenseService.getRejectedExpenses();
        }

        for(RecurringExpense expense : totalRecExpenseList){
            response.add(dtoMapperService.convertToRecExpenseDTO(expense));
        }
        for(OneTimeExpense expense: totalOneTimeExpenseList){
            response.add(dtoMapperService.convertToMockRecExpenseDTO(expense));
        }

        return response;
    }

}
