package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecurringExpenseService {
    @Autowired
    private RecurringExpenseRepository repository;

    public RecurringExpense createRecurringExpense(RecurringExpense expense){
        return repository.save(expense);
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

    public void deleteRecurringExpense(Integer id){
        repository.deleteById(id);
    }
}
