package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.UpdateBudgetDTO;
import com.example.BudgetBuddy.Exceptions.BudgetNotFoundException;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Notification;
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
    private NotificationService notificationService;

    public Budget createBudget(Budget newBudget){
        Budget savedBudget =  budgetRepo.save(newBudget);
        notificationService.createNotification(new Notification(
                "Budget Submission",
                "New budget %s submitted for approval".formatted(savedBudget.getName()),
                savedBudget.getDepartment().getName()
                ));
        return savedBudget;
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
        Budget savedBudget = budgetRepo.save(budget);
        notificationService.createNotification(new Notification(
                "Budget Approval",
                "Budget %s has been approved".formatted(savedBudget.getName()),
                savedBudget.getDepartment(),
                "Admin"
        ));
        return savedBudget;
    }

    public Budget rejectBudget(Long id, String message){
        Budget budget = budgetRepo.findById(id).orElseThrow(() -> new RuntimeException("Budget with this ID does not exist."));
        budget.setStatus(Budget.Status.Rejected);
        Budget savedBudget = budgetRepo.save(budget);
        notificationService.createNotification(new Notification(
                "Budget Rejection",
                "Budget %s has been rejected because: %s.".formatted(savedBudget.getName(),message),
                savedBudget.getDepartment(),
                "Admin"
        ));
        return budget;
    }

    public void deleteBudget(Long id){
        budgetRepo.deleteById(id);

    }

    public List<Budget> getApprovedBudgets(){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Approved)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<Budget> getPendingBudgets(){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Pending)){
                response.add(budget);
            }
        }
        return response;
    }

    public List<Budget> getRejectedBudgets(){
        List<Budget> budgets = budgetRepo.findAll();
        List<Budget> response = new ArrayList<>();
        for(Budget budget : budgets){
            if(budget.getStatus().equals(Budget.Status.Rejected)){
                response.add(budget);
            }
        }
        return response;
    }

}
