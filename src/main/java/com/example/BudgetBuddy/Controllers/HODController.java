package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.HODExpenseChart;
import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Services.*;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/hods")
public class HODController {

    @Autowired
    private HODService hodService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @Autowired
    private ExpenseSummariser expenseSummariser;

//    @GetMapping(path = "/{id}/notifications")
//    public ResponseEntity<List<Notification>> getNotificationsForHOD(@PathVariable(name = "id") Long hodID){
//        return new ResponseEntity<>(notificationService.getNotificationsByDepartment(departmentService.getDepartmentById(hodID).getBody().getName()), HttpStatus.OK);
//    }

    @GetMapping(path = "/dashboard/total-budget-count")
    public ResponseEntity<Integer> getBudgetCounts(){
//    Came about this rudimentary way to grab the HOD's department ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        Integer response = departmentService.getAllBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/approved-budget-count")
    public ResponseEntity<Integer> getApprovedBudgetCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        Integer response = departmentService.getApprovedBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/pending-budget-count")
    public ResponseEntity<Integer>getPendingBudgetCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        Integer response = departmentService.getPendingBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "dashboard/rejected-budget-count")
    public ResponseEntity<Integer> getRejectedBudgetCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        Integer response = departmentService.getRejectedBudgets(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/total-recurring-expense-count")
    public ResponseEntity<Integer> getTotalRecurringExpenseCount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        Integer response = departmentService.getRecurringExpenses(departmentId).size();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dashboard/expense-chart")
    public ResponseEntity<HODExpenseChart> getHODExpenseChart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
            if (hod != null){
                departmentId = hod.getDepartment().getId();
            }
        }

        HODExpenseChart expenseChart = new HODExpenseChart(
                expenseSummariser.getMonthlySummary(departmentService.getRecurringExpenses(departmentId), departmentId),
                expenseSummariser.getYearlySummary(departmentService.getRecurringExpenses(departmentId), departmentId)
        );
        return new ResponseEntity<>(expenseChart, HttpStatus.OK);
    }

}
