package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.RecExpenseDTO;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.DTOMapperService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "recurringexpenses")
public class RecurringExpenseController {
    @Autowired
    private RecurringExpenseService service;

    @Autowired
    private DTOMapperService dtoMapperService;

    @PostMapping(path = "/create")
    public RecurringExpense createRecurringExpense(RecurringExpense expense, Long budgetId){
        return service.createRecurringExpense(expense, budgetId);
    }

    /*
    Endpoint to retrieve all recurring expenses
     */
    @GetMapping
    public List<RecExpenseDTO> getRecurringExpenses(){
        List<RecurringExpense> result =  service.getRecurringExpenses();
        List<RecExpenseDTO> response = new ArrayList<>();

        for(RecurringExpense expense : result){
            response.add(dtoMapperService.convertToRecExpenseDTO(expense));
        }
        return response;
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

    @PutMapping(path = "/{id}/approve")
    public RecExpenseDTO approveExpense(Integer expenseId){
        return dtoMapperService.convertToRecExpenseDTO(service.approveExpense(expenseId));
    }

    @PutMapping(path = "/{id}/reject")
    public RecExpenseDTO rejectExpense(Integer expenseId){
        return dtoMapperService.convertToRecExpenseDTO(service.rejectExpense(expenseId));
    }

}
