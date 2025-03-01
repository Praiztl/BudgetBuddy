package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.UpdateBudgetDTO;
import com.example.BudgetBuddy.Exceptions.BudgetNotFoundException;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.HODNotification;
import com.example.BudgetBuddy.Repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepo;

    @Autowired
    private HODNotificationService hodNotificationService;

    public Budget createBudget(Budget newBudget){
        return budgetRepo.save(newBudget);
    }

    public List<Budget> getAllBudgets(){
        return budgetRepo.findAll();
    }


    public Budget getBudgetById(Long id){
        return budgetRepo.findById(id).orElseThrow(()->new BudgetNotFoundException("Budget with this ID does not exist."));
    }

    public Budget updateBudget(Long id, UpdateBudgetDTO updateBody){
        Budget budget = budgetRepo.findById(id).orElseThrow(()->new BudgetNotFoundException("Budget with this ID does not exist."));

        if(updateBody.getName() != null) {
            budget.setName(updateBody.getName());
        }
        if(updateBody.getAmount() != null) {
            budget.setAmount(updateBody.getAmount());
        }
        return budgetRepo.save(budget);
    }

    public Budget approveBudget(Long id){
        Budget budget = budgetRepo.findById(id).orElseThrow(() -> new RuntimeException("Budget with this ID does not exist."));
        budget.setStatus(Budget.Status.Approved);
        budget = budgetRepo.save(budget);
        hodNotificationService.createNotification(new HODNotification("Status Updated", "The budget %s's status has been updated to %s.".formatted(budget.getName(), budget.getStatus()), budget.getDepartment().getHod()));
        return budget;
    }

    public Budget rejectBudget(Long id){
        Budget budget = budgetRepo.findById(id).orElseThrow(() -> new RuntimeException("Budget with this ID does not exist."));
        budget.setStatus(Budget.Status.Rejected);
        budget = budgetRepo.save(budget);
        hodNotificationService.createNotification(new HODNotification("Status Updated", "The budget %s's status has been updated to %s.".formatted(budget.getName(), budget.getStatus()), budget.getDepartment().getHod()));
        return budget;
    }

    public void deleteBudget(Long id){
        budgetRepo.deleteById(id);

    }
}
