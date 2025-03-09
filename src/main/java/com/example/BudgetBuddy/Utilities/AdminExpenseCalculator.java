package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminExpenseCalculator {
    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @Autowired
    private BudgetService budgetService;


    public Double getMonthlyAmountRecurringExpenses(String monthName, Long orgId){
        List<RecurringExpense> expenses = recurringExpenseService.getApprovedExpensesForOrg(orgId);
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

    public Double getYearlyAmountRecurringExpenses(Integer year, Long orgId){
        List<RecurringExpense> expenses = recurringExpenseService.getApprovedExpensesForOrg(orgId);
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

    public Double getMonthlyAmountOneTimeExpenses(String monthName, Long orgId){
        List<OneTimeExpense> expenses = oneTimeExpenseService.getForOrganization(orgId);
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

    public Double getYearlyAmountOneTimeExpenses(Integer year, Long orgId){
        List<OneTimeExpense> expenses = oneTimeExpenseService.getForOrganization(orgId);
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

    public Map<Integer, Double> calculateYearlyBudgetAmount(Integer year, List<Budget> budgetList){
        Map<Integer, Double> yearlyBudgetAmountMap = new HashMap<>();
        Double yearlyBudgetAmount = 0.0;
//        List<Budget> budgetList = budgetService.getAllBudgets();
        List<Budget> yearlyBudgetList = new ArrayList<>();

        // Initialising a list of budgets for specified year
        for(Budget budget : budgetList){
            if(budget.getCreatedAt().getYear()==year){
                yearlyBudgetList.add(budget);
            }
        }

        //Getting total of budget amounts in the year
        for(Budget budget : yearlyBudgetList){
            yearlyBudgetAmount+= budget.getAmount();
        }

        //Returning a mapping of year to yearly budget amount
        yearlyBudgetAmountMap.put(year, yearlyBudgetAmount);
        return yearlyBudgetAmountMap;
    }

}
