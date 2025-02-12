package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Exceptions.BudgetNotFoundException;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepo;

    public Budget createBudget(Budget newBudget){
        return budgetRepo.save(newBudget);
    }

    public List<Budget> getAllBudgets(){
        return budgetRepo.findAll();
    }

    public Budget getBudgetById(String id){
        return budgetRepo.findById(id).orElseThrow(()->new BudgetNotFoundException("Budget with this ID does not exist."));
    }

    public Budget updateBudget(String id, Budget updateBody){
        Budget budget = budgetRepo.findById(id).orElseThrow(()->new BudgetNotFoundException("Budget with this ID does not exist."));

        if(!budget.equals(updateBody)){
            budget = updateBody;
        }
        return budgetRepo.save(budget);
    }

    public void deleteBudget(String id){
        budgetRepo.deleteById(id);
    }
}
