package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.*;
import com.example.BudgetBuddy.Utilities.AdminExpenseCalculator;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(path = "/notifications")
    public ResponseEntity<List<Notification>> getNotificationsForaAdmin(){
        return new ResponseEntity<>(notificationService.getNotificationsForAdmin(), HttpStatus.OK);
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

    @GetMapping(path = "/dashboard/get-total-budget-count")
    public ResponseEntity<Integer> getTotalBudgetCount(@RequestParam(name = "status", required = false)Budget.Status status){
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

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-recurring-expense-count")
    public ResponseEntity<Map<String, Integer>> getRecurringExpenseCount(){
        List<Department> departments = departmentService.getAllDepartments().getBody();
        Map<String, Integer> departmentCounts = new HashMap<>();
        assert departments != null;
        for(Department department: departments){
            Integer recurringExpenseCount = departmentService.getRecurringExpenses(department.getId()).size();
            departmentCounts.put(department.getName(), recurringExpenseCount);
        }

        return new ResponseEntity<>(departmentCounts, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-total-recurring-expense-count")
    public ResponseEntity<Integer> getTotalRecurringExpenseCount(@RequestParam(name = "status", required = false) RecurringExpense.Status status){
        Integer count = 0;
        if(status == null) {
            recurringExpenseService.getRecurringExpenses().size();
        }
        if(status.equals(RecurringExpense.Status.Approved)){
            count = recurringExpenseService.getApprovedExpenses().size();
        } else if (status.equals(RecurringExpense.Status.Pending)){
            count = recurringExpenseService.getPendingExpenses().size();
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            count = recurringExpenseService.getRejectedExpenses().size();
        }

        return new ResponseEntity<>(count, HttpStatus.OK);
    }


    @GetMapping(path = "/dashboard/get-department-count")
    public ResponseEntity<Integer> getDepartmentCount(){
        Integer departmentCount = departmentService.getAllDepartments().getBody().size();
        return new ResponseEntity<>(departmentCount, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-expense-chart")
    public ResponseEntity<HODExpenseChart> getExpenseChart(){
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
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-yearly-budget-total")
    public ResponseEntity<Map<Integer, Double>> getYearlyBudgetTotal(){
        List<Budget> budgetList = budgetService.getApprovedBudgets();
        Map<Integer, Double> response = expenseSummariser.getAdminYearlyBudgetTotal(budgetList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-total-budget-list")
    public ResponseEntity<List<BudgetDTO>> getTotalBudgetList(@RequestParam(name = "status", required = false)Budget.Status status){
        List<BudgetDTO> totalBudgetDTOList = new ArrayList<>();
        List<Budget> totalBudgetList = new ArrayList<>();

        if(status == null) {
            totalBudgetList = budgetService.getAllBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }
        else if(status.equals(RecurringExpense.Status.Approved)){
            totalBudgetList = budgetService.getApprovedBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(RecurringExpense.Status.Pending)){
            totalBudgetList = budgetService.getPendingBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            totalBudgetList = budgetService.getRejectedBudgets();
            for(Budget budget : totalBudgetList){
                totalBudgetDTOList.add(dtoMapperService.convertToBudgetDTO(budget));
            }
        }

        return new ResponseEntity<>(totalBudgetDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/get-total-recurring-expense-list")
    public ResponseEntity<List<RecurringExpense>> getTotalRecurringExpenseList(@RequestParam(name = "status", required = false) RecurringExpense.Status status){
        List<RecurringExpense> totalRecExpenseList = new ArrayList<>();

        if(status == null) {
            totalRecExpenseList = recurringExpenseService.getRecurringExpenses();
        }
        else if(status.equals(RecurringExpense.Status.Approved)){
            totalRecExpenseList = recurringExpenseService.getApprovedExpenses();
        } else if (status.equals(RecurringExpense.Status.Pending)){
            totalRecExpenseList = recurringExpenseService.getPendingExpenses();
        } else if (status.equals(RecurringExpense.Status.Rejected)) {
            totalRecExpenseList = recurringExpenseService.getRejectedExpenses();
        }

        return new ResponseEntity<>(totalRecExpenseList, HttpStatus.OK);
    }

}
