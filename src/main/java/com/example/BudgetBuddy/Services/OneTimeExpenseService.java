package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Repositories.OneTimeExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OneTimeExpenseService {
    @Autowired
    private OneTimeExpenseRepository repository;

    @Autowired
    private BudgetService budgetService;

    public ResponseEntity<OneTimeExpense> createOneTimeExpense(OneTimeExpense expense, String budgetId){
        if (expense.getAssignedTo() == null) {
            expense.setAssignedTo(budgetService.getBudgetById(budgetId));
            OneTimeExpense response = repository.save(expense);
            return new ResponseEntity<OneTimeExpense>(response, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
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

    public ResponseEntity<List<OneTimeExpense>> getForBudget(String budgetId){
        List<OneTimeExpense> expenses = new ArrayList<>();
        for (OneTimeExpense expense: repository.findAll()){
            if(expense.getAssignedTo().getId().equals(budgetId)){
                expenses.add(expense);
            }
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    public void deleteExpense(Integer id){
        repository.deleteById(id);
    }

    /*
    It is not expected that users will edit expenses
     */
}
