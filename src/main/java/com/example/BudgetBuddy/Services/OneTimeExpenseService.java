package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Repositories.OneTimeExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OneTimeExpenseService {
    @Autowired
    private OneTimeExpenseRepository repository;

    public OneTimeExpense createOneTimeExpense(OneTimeExpense expense){
        return repository.save(expense);
    }

    /*
    To retrieve a single one-time expense
     */
    public OneTimeExpense getOneTimeExpense(Integer id){
        return repository.findById(id).orElse(null);
    }

    /*
    To retrieve all one-time expenses
     */
    public List<OneTimeExpense> getOneTimeExpenses(){
        return repository.findAll();
    }

    public void deleteExpense(Integer id){
        repository.deleteById(id);
    }

    /*
    It is not expected that users will edit expenses
     */
}
