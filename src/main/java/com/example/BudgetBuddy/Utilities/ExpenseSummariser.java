package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseSummariser {

    @Autowired
    private DepartmentService departmentService;


    public List<Map<String, Object>> getMonthlySummary(List<RecurringExpense> expenseList, Long departmentId) {
        List<Map<String, Object>> monthlySummary = new ArrayList<>();

        for (RecurringExpense expense : expenseList) {
            Map<String, Object>oneSummary = new HashMap<>();
            String monthValue = expense.getCreatedAt().getMonth().name().substring(0, 3);

            oneSummary.put("month", monthValue);
            oneSummary.put("amount", departmentService.getMonthlyAmountOneTimeExpenses(departmentId, monthValue)+departmentService.getMonthlyAmountRecurringExpenses(departmentId, monthValue));
            monthlySummary.add(oneSummary);
        }

            return monthlySummary;
    }


    public List<Map<String, Object>> getYearlySummary(List<RecurringExpense> expenseList, Long departmentId) {
        List<Map<String, Object>> yearlySummary = new ArrayList<>();

        for (RecurringExpense expense : expenseList) {
            Map<String, Object> oneSummary = new HashMap<>();
            Integer yearValue = expense.getCreatedAt().getYear();

            oneSummary.put("year", yearValue);
            oneSummary.put("amount", departmentService.getYearlyAmountOneTimeExpenses(departmentId, yearValue) +departmentService.getYearlyAmountRecurringExpenses(departmentId, yearValue));
            yearlySummary.add(oneSummary);
        }

        return yearlySummary;
    }

    public Map<String, Object> currentMonthSummary(List<RecurringExpense> expenseList, String monthValue) {
        if(monthValue==null){
            monthValue=LocalDate.now().getMonth().name().substring(0,3);
        }
        List<Map<String, Object>> monthlySummary = new ArrayList<>();
        Map<String, Object> summaryItem = new HashMap<>();

        for (RecurringExpense expense : expenseList
//                .stream()
//                .sorted(Comparator.comparingDouble(RecurringExpense::getAmount).reversed()) // Sort by amount in descending order
//                .limit(5) // Take the top 5
//                .collect(Collectors.toList())
        ) {
            Map<String, Double> summaryItemDetail = new HashMap<>();

            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);
            int year = expense.getCreatedAt().getYear();
            String expenseName = expense.getName();
            Double amount = expense.getAmount();

            if(monthKey.equals(monthValue) && year == LocalDate.now().getYear()){
                summaryItemDetail.put(expenseName, summaryItemDetail.getOrDefault(expenseName, 0.0) + amount);
            }
            summaryItem.put(monthKey, summaryItemDetail);
            monthlySummary.add(summaryItem);
        }

        return summaryItem; // Return the summary as a list
    }
}