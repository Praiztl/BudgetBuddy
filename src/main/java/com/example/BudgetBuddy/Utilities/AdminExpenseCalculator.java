package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdminExpenseCalculator {
    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;


    public Double getMonthlyAmountRecurringExpenses(String monthName){
        List<RecurringExpense> expenses = recurringExpenseService.getRecurringExpenses();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(RecurringExpense expense : expenses){
            if(expense.getCreatedAt().getYear() == LocalDate.now().getYear()){
                response.add(expense);
            }
        }
        for(RecurringExpense expense : response){
            if(expense.getCreatedAt().getMonth().name().substring(0,3).equals(monthName)){
                if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                    total+=(expense.getAmount()*4);
                }
                else if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                    total += expense.getAmount()*(expense.getCreatedAt().getMonth().length(expense.getCreatedAt().isLeapYear()));
                }
                else {
                    total += expense.getAmount();
                }
            }
        }
        return total;
    }

    public Double getYearlyAmountRecurringExpenses(Integer year){
        List<RecurringExpense> expenses = recurringExpenseService.getRecurringExpenses();
        List<RecurringExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(RecurringExpense expense : expenses){
            if(expense.getCreatedAt().getYear()==year){
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
                }else{
                    total += expense.getAmount();
                }
            }
        }
        return total;
    }

    public Double getMonthlyAmountOneTimeExpenses(String monthName){
        List<OneTimeExpense> expenses = oneTimeExpenseService.getOneTimeExpenses();
        List<OneTimeExpense> response = new ArrayList<>();
        Double total = 0.0;
        for(OneTimeExpense expense : expenses){
            if(expense.getCreatedAt().getYear()==LocalDate.now().getYear()){
                response.add(expense);
            }
        }
        for(OneTimeExpense expense : response){
            if(expense.getCreatedAt().getMonth().name().substring(0,3).equals(monthName)){
                total += expense.getAmount();
            }
        }
        return total;
    }

    public Double getYearlyAmountOneTimeExpenses(Integer year){
        List<OneTimeExpense> expenses = oneTimeExpenseService.getOneTimeExpenses();
        List<OneTimeExpense> response = new ArrayList<>();
        Double total = 0.0;
//        for(OneTimeExpense expense : expenses){
//            if(Objects.equals(expense.getAssignedTo().getDepartment().getId(), departmentId)){
//                response.add(expense);
//            }
//        }
        for(OneTimeExpense expense : expenses){
            if(expense.getCreatedAt().getYear()==year){
                total += expense.getAmount();
            }
        }
        return total;
    }

}
