package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "recurringexpenses")
public class RecurringExpenseController {
    @Autowired
    private RecurringExpenseService service;

    @PostMapping(path = "/create")
    public RecurringExpense createRecurringExpense(RecurringExpense expense, Long budgetId){
        return service.createRecurringExpense(expense, budgetId);
    }

    /*
    Endpoint to retrieve all recurring expenses
     */
    @GetMapping
    public List<RecurringExpense> getRecurringExpenses(){
        return service.getRecurringExpenses();
    }

    /*
    Endpoint to retrieve a specific recurring expense
     */
    @GetMapping(path = "/{id}")
    public RecurringExpense getRecurringExpense(@PathVariable(name = "id") Integer id){
        return service.getRecurringExpense(id);
    }

    @DeleteMapping(path = "/{id}/delete")
    public String deleteRecurringExpense(@PathVariable(name = "id") Integer id){
        service.deleteRecurringExpense(id);
        return "Recurring expense %s deleted.".formatted(id);
    }

}
