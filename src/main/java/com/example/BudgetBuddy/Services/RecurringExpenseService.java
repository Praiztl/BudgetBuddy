package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.BudgetBuddy.Models.RecurringExpense.Interval;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringExpenseService {
    @Autowired
    private RecurringExpenseRepository repository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private NotificationService notificationService;

    public RecurringExpense createRecurringExpense(RecurringExpense expense, Long budgetId){
        expense.setAssignedTo(budgetService.getBudgetById(budgetId));
        expense.setCreatedAt(LocalDate.now());
        expense.setApprovalStatus(RecurringExpense.Status.Pending);
        RecurringExpense savedExpense = repository.save(expense);
        notificationService.createNotification(new Notification(
                "Recurring Expense Submission",
                "New recurring expense %s submitted for approval.".formatted(savedExpense.getName())
        ));
        return savedExpense;
    }

    /*
    Method to retrieve all recurring expenses
     */
    public List<RecurringExpense> getRecurringExpenses(){
        return repository.findAll();
    }

    /*
    Method to retrieve a single recurring expense
     */
    public RecurringExpense getRecurringExpense(Integer id){
        return repository.findById(id).orElse(null);
    }

    /*
    It is assumed that no expenses will be updated
     */

    public RecurringExpense approveExpense(Integer id){
        RecurringExpense expense = repository.findById(id).orElseThrow(() -> new RuntimeException("Recurring expense with this ID does not exist."));
        expense.setApprovalStatus(RecurringExpense.Status.Approved);
        RecurringExpense savedExpense = repository.save(expense);
        notificationService.createNotification(new Notification(
                "Recurring Expense Approval",
                "Recurring expense %s has been approved".formatted(savedExpense.getName()),
                savedExpense.getAssignedTo().getDepartment().getName()
        ));
        return savedExpense;
    }

    public RecurringExpense rejectExpense(Integer id){
        RecurringExpense expense = repository.findById(id).orElseThrow(() -> new RuntimeException("Recurring expense with this ID does not exist."));
        expense.setApprovalStatus(RecurringExpense.Status.Rejected);
        RecurringExpense savedExpense = repository.save(expense);
        notificationService.createNotification(new Notification(
                "Recurring Expense Rejection",
                "Recurring expense %s has been rejected".formatted(savedExpense.getName()),
                savedExpense.getAssignedTo().getDepartment().getName()
        ));
        return savedExpense;
    }


    public void deleteRecurringExpense(Integer id){
        repository.deleteById(id);
    }
}
