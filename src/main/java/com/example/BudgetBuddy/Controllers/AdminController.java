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

    @GetMapping(path = "/{orgId}/notifications")
    public ResponseEntity<?> getNotificationsForaAdmin(@PathVariable(name = "orgId")Long orgId){
        Map<String, List<NotificationDTO>> notificationMapping = new HashMap<>();

        List<NotificationDTO> allNotifications = notificationService.getNotificationsForAdmin(orgId);
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

    @GetMapping(path = "/{orgId}/dashboard/get-budget-count-per-department")
    public ResponseEntity<Map<String, Integer>> getBudgetPerDepartment(@PathVariable(name = "orgId") Long orgId){
        List<Department> departments = departmentService.getDepartmentsForOrganization(orgId);
        Map<String, Integer> departmentCounts = new HashMap<>();
        assert departments != null;
        for(Department department: departments){
            Integer budgetCount = departmentService.getAllBudgets(department.getId()).size();
            departmentCounts.put(department.getName(), budgetCount);
        }

        return new ResponseEntity<>(departmentCounts, HttpStatus.OK);
    }

    @GetMapping(path = "/{orgId}/dashboard/get-budget-statistics")
    public ResponseEntity<?> getBudgetStatistics(@PathVariable(name = "orgId")Long orgId){         //Map<String, Integer>
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON ).body(adminService.getBudgetStatistics(orgId));
    }

    @GetMapping(path = "/{orgId}/dashboard/get-total-budget-count")
    public Integer getTotalBudgetCount(@PathVariable(name = "orgId") Long orgId, @RequestParam(name = "status", required = false)Budget.Status status){
        Integer count = 0;
        if(status==null){
            count = budgetService.getAllBudgetsForOrg(orgId).size();
        }
        else if(status.equals(Budget.Status.Approved)){
            count = budgetService.getApprovedBudgetsForOrg(orgId).size();
        } else if (status.equals(Budget.Status.Pending)){
            count = budgetService.getPendingBudgetsForOrg(orgId).size();
        } else if (status.equals(Budget.Status.Rejected)) {
            count = budgetService.getRejectedBudgetsForOrg(orgId).size();
        }

       return count;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-recurring-expense-count")
    public Map<String, Integer> getRecurringExpenseCount(@PathVariable(name = "orgId") Long orgId){
        List<Department> departments = departmentService.getDepartmentsForOrganization(orgId);
        Map<String, Integer> departmentCounts = new HashMap<>();
        assert departments != null;
        for(Department department: departments){
            Integer recurringExpenseCount = departmentService.getRecurringExpenses(department.getId()).size();
            departmentCounts.put(department.getName(), recurringExpenseCount);
        }

        return departmentCounts;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-total-recurring-expense-count")
    public Integer getTotalRecurringExpenseCount(@PathVariable(name = "orgId")Long orgId, @RequestParam(name = "status", required = false) RecurringExpense.Status status) {
        Integer count = 0;
        if (status == null) {
            count = recurringExpenseService.getRecurringExpensesForOrg(orgId).size();
        }
        else if (status.equals(RecurringExpense.Status.Approved)) {
            count = recurringExpenseService.getApprovedExpensesForOrg(orgId).size();
        } else if (status.equals(RecurringExpense.Status.Pending)) {
            count = recurringExpenseService.getPendingExpensesForOrg(orgId).size();
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            count = recurringExpenseService.getRejectedExpensesForOrg(orgId).size();
        }

        return count;
    }


    @GetMapping(path = "/{orgId}/dashboard/get-department-count")
    public Integer getDepartmentCount(@PathVariable(name = "orgId") Long orgId){
        Integer departmentCount = departmentService.getDepartmentsForOrganization(orgId).size();
        return departmentCount;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-expense-chart")
    public HODExpenseChart getExpenseChart(@PathVariable(name = "orgId")Long orgId){
        List<Expense> expenseList = new ArrayList<>();
        for(OneTimeExpense expense: oneTimeExpenseService.getForOrganization(orgId)){
            expenseList.add(expense);
        }
        for(RecurringExpense expense: recurringExpenseService.getApprovedExpensesForOrg(orgId)){
            expenseList.add(expense);
        }
        HODExpenseChart response = new HODExpenseChart(
                expenseSummariser.getAdminMonthlySummary(expenseList, orgId),
                expenseSummariser.getAdminYearlySummary(expenseList, orgId)
        );
        return response;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-yearly-budget-total")
    public Map<Integer, Double> getYearlyBudgetTotal(@PathVariable(name = "orgId") Long orgId){
        List<Budget> budgetList = budgetService.getApprovedBudgetsForOrg(orgId);
        Map<Integer, Double> response = expenseSummariser.getAdminYearlyBudgetTotal(budgetList);
        return response;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-total-budget-list")
    public List<BudgetDTO> getTotalBudgetList(@RequestParam(name = "status", required = false)Budget.Status status, @PathVariable(name = "orgId") Long orgId){
        List<BudgetDTO> totalBudgetDTOList = new ArrayList<>();
        List<Budget> totalBudgetList = new ArrayList<>();

        if(status == null) {
            totalBudgetList = budgetService.getAllBudgetsForOrg(orgId);
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }
        else if(status.equals(Budget.Status.Approved)){
            totalBudgetList = budgetService.getApprovedBudgetsForOrg(orgId);
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(Budget.Status.Pending)){
            totalBudgetList = budgetService.getPendingBudgetsForOrg(orgId);
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(Budget.Status.Rejected)) {
            totalBudgetList = budgetService.getRejectedBudgetsForOrg(orgId);
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }

        return totalBudgetDTOList;
    }

    @GetMapping(path = "/{orgId}/dashboard/get-total-recurring-expense-list")
    public List<Object> getTotalRecurringExpenseList(@PathVariable(name = "orgId") Long orgId, @RequestParam(name = "status", required = false) RecurringExpense.Status status){
        List<RecurringExpense> totalRecExpenseList = new ArrayList<>();
        List<OneTimeExpense> totalOneTimeExpenseList = new ArrayList<>();
        List<Object> response = new ArrayList<>();

        if(status == null) {
            totalRecExpenseList=recurringExpenseService.getRecurringExpensesForOrg(orgId);
            totalOneTimeExpenseList=oneTimeExpenseService.getForOrganization(orgId);
        }
        else if(status.equals(RecurringExpense.Status.Approved)){
            totalRecExpenseList=recurringExpenseService.getApprovedExpensesForOrg(orgId);
            totalOneTimeExpenseList=oneTimeExpenseService.getForOrganization(orgId);
        } else if (status.equals(RecurringExpense.Status.Pending)){
            totalRecExpenseList=recurringExpenseService.getPendingExpensesForOrg(orgId);
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            totalRecExpenseList=recurringExpenseService.getRejectedExpensesForOrg(orgId);
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
