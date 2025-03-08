package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.RecurringExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecurringExpenseService {
    @Autowired
    private RecurringExpenseRepository repository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private NotificationService notificationService;

    public RecurringExpense createRecurringExpense(RecurringExpense expense, Long departmentId){
        expense.setAssignedTo(departmentService.getDepartmentById(departmentId).getBody());
        expense.setCreatedAt(LocalDate.now());
        expense.setApprovalStatus(RecurringExpense.Status.Pending);
        expense.setBudget((departmentService.getApprovedBudgets(departmentId).get(0).getDepartmentName()));
        RecurringExpense savedExpense = repository.save(expense);
        notificationService.createNotification(new Notification(
                "Recurring Expense Submission",
                "New recurring expense %s submitted for approval.".formatted(savedExpense.getName()),
                savedExpense.getAssignedTo().getName()
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
                savedExpense.getAssignedTo(),
                "Admin"
        ));
        return savedExpense;
    }

    public RecurringExpense rejectExpense(Integer id, String message){
        RecurringExpense expense = repository.findById(id).orElseThrow(() -> new RuntimeException("Recurring expense with this ID does not exist."));
        expense.setApprovalStatus(RecurringExpense.Status.Rejected);
        RecurringExpense savedExpense = repository.save(expense);
        notificationService.createNotification(new Notification(
                "Recurring Expense Rejection",
                "Recurring expense %s has been rejected because: %s.".formatted(savedExpense.getName(),message),
                savedExpense.getAssignedTo(),
                "Admin"
        ));
        return savedExpense;
    }


    public void deleteRecurringExpense(Integer id){
        repository.deleteById(id);
    }


    public List<RecurringExpense> getApprovedExpenses(){
        List<RecurringExpense> expenses = repository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        for(RecurringExpense expense : expenses){
            if(expense.getApprovalStatus().equals(RecurringExpense.Status.Approved)){
                response.add(expense);
            }
        }
        return response;
    }

    public List<RecurringExpense> getPendingExpenses(){
        List<RecurringExpense> expenses = repository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        for(RecurringExpense expense : expenses){
            if(expense.getApprovalStatus().equals(RecurringExpense.Status.Pending)){
                response.add(expense);
            }
        }
        return response;
    }

    public List<RecurringExpense> getRejectedExpenses(){
        List<RecurringExpense> expenses = repository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        for(RecurringExpense expense : expenses){
            if(expense.getApprovalStatus().equals(RecurringExpense.Status.Rejected)){
                response.add(expense);
            }
        }
        return response;
    }

    public Double getMonthlyAmountRecurringExpenses(String monthName){
        List<RecurringExpense> expenses = repository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(RecurringExpense expense : expenses){
            if(expense.getCreatedAt().getYear() == LocalDate.now().getYear()){
                response.add(expense);
            }
        }
        for(RecurringExpense expense : response){
            if(expense.getCreatedAt().getMonth().name().substring(0,3).equals(monthName)){
                total += expense.getAmount();
                if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                    total+=(expense.getAmount()*4);
                }
                else if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                    total += expense.getAmount()*(expense.getCreatedAt().getMonth().length(expense.getCreatedAt().isLeapYear()));
                }
            }
        }
        return total;
    }

    public Double getYearlyAmountRecurringExpenses(Integer year){
        List<RecurringExpense> expenses = repository.findAll();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
//        for(RecurringExpense expense : expenses){
//            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
//                response.add(expense);
//            }
//        }
        for(RecurringExpense expense : expenses){
            if(expense.getCreatedAt().getYear()==year){
                total += expense.getAmount();
                if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                    total+=(expense.getAmount()*52);
                }
                else if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                    if(expense.getCreatedAt().isLeapYear()){
                        total += expense.getAmount()*366;
                    }else{
                        total += expense.getAmount()*365;
                    }
                } else if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Monthly)) {
                    total += expense.getAmount()*12;
                }
            }
        }
        return total;
    }


}
