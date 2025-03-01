package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.HODDashboardCounts;
import com.example.BudgetBuddy.Models.HODNotification;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.HODNotificationService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/hods")
public class HODController {
    @Autowired
    private HODNotificationService notificationService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @GetMapping(path = "/{id}/notifications")
    public ResponseEntity<List<HODNotification>> getNotificationsForHOD(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(notificationService.getNotificationsForHOD(id), HttpStatus.OK);
    }

}
