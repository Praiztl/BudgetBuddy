package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.Expense;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Services.DepartmentService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseSummariser {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    AdminExpenseCalculator adminExpenseCalculator;

    public List<Map<String, Object>> getMonthlySummary(List<Expense> expenseList, Long departmentId) {
        List<Map<String, Object>> monthlySummary = new ArrayList<>();

        for (Expense expense : expenseList) {
            Map<String, Object>oneSummary = new HashMap<>();
            String monthValue = expense.getCreatedAt().getMonth().name().substring(0, 3);

            oneSummary.put("month", monthValue);
            oneSummary.put("amount", departmentService.getMonthlyAmountOneTimeExpenses(departmentId, monthValue)+departmentService.getMonthlyAmountRecurringExpenses(departmentId, monthValue));
            boolean found = false;
            for (Map<String, Object> existingSummary : monthlySummary) {
                if (existingSummary.get("month").equals(monthValue)) {
                    Double existingAmount = (Double) existingSummary.get("amount");
                    existingSummary.put("amount", departmentService.getMonthlyAmountOneTimeExpenses(departmentId, monthValue)+departmentService.getMonthlyAmountRecurringExpenses(departmentId, monthValue));
                    found = true;
                    break;
                }
            }

            if (!found) {
                monthlySummary.add(oneSummary);
            }        }

            return monthlySummary;
    }

    public List<Map<String, Object>> getAdminMonthlySummary(List<Expense> expenseList, Long orgId) {
        List<Map<String, Object>> monthlySummary = new ArrayList<>();

        for (Expense expense : expenseList) {
            Map<String, Object>oneSummary = new HashMap<>();
            String monthValue = expense.getCreatedAt().getMonth().name().substring(0, 3);

            oneSummary.put("month", monthValue);
            oneSummary.put("amount", adminExpenseCalculator.getMonthlyAmountRecurringExpenses(monthValue, orgId)+adminExpenseCalculator.getMonthlyAmountOneTimeExpenses(monthValue, orgId));
            boolean found = false;
            for (Map<String, Object> existingSummary : monthlySummary) {
                if (existingSummary.get("month").equals(monthValue)) {
                    Double existingAmount = (Double) existingSummary.get("amount");
                    existingSummary.put("amount", adminExpenseCalculator.getMonthlyAmountOneTimeExpenses(monthValue, orgId)+adminExpenseCalculator.getMonthlyAmountRecurringExpenses(monthValue, orgId));
                    found = true;
                    break;
                }
            }

            if (!found) {
                monthlySummary.add(oneSummary);
            }
        }

        return monthlySummary;
    }


    public List<Map<String, Object>> getYearlySummary(List<Expense> expenseList, Long departmentId) {
        List<Map<String, Object>> yearlySummary = new ArrayList<>();

        for (Expense expense : expenseList) {
            Map<String, Object> oneSummary = new HashMap<>();
            Integer yearValue = expense.getCreatedAt().getYear();

            oneSummary.put("year", yearValue);
            oneSummary.put("amount", departmentService.getYearlyAmountOneTimeExpenses(departmentId, yearValue) + departmentService.getYearlyAmountRecurringExpenses(departmentId, yearValue));
            boolean found = false;
            for (Map<String, Object> existingSummary : yearlySummary) {
                if (existingSummary.get("year").equals(yearValue)) {
                    Double existingAmount = (Double) existingSummary.get("amount");
                    existingSummary.put("amount", departmentService.getYearlyAmountOneTimeExpenses(departmentId, yearValue) + departmentService.getYearlyAmountRecurringExpenses(departmentId, yearValue));
                    found = true;
                    break;
                }
            }

            if (!found) {
                yearlySummary.add(oneSummary);
            }
        }

        return yearlySummary;
    }

    public List<Map<String, Object>> getAdminYearlySummary(List<Expense> expenseList, Long orgId) {
        List<Map<String, Object>> yearlySummary = new ArrayList<>();

        for (Expense expense : expenseList) {
            Map<String, Object> oneSummary = new HashMap<>();
            Integer yearValue = expense.getCreatedAt().getYear();

            oneSummary.put("year", yearValue);
            oneSummary.put("amount", adminExpenseCalculator.getYearlyAmountRecurringExpenses(yearValue, orgId) +adminExpenseCalculator.getYearlyAmountOneTimeExpenses(yearValue, orgId));
            boolean found = false;
            for (Map<String, Object> existingSummary : yearlySummary) {
                if (existingSummary.get("year").equals(yearValue)) {
                    Double existingAmount = (Double) existingSummary.get("amount");
                    existingSummary.put("amount", adminExpenseCalculator.getYearlyAmountOneTimeExpenses(yearValue, orgId)+adminExpenseCalculator.getYearlyAmountRecurringExpenses(yearValue, orgId));
                    found = true;
                    break;
                }
            }

            if (!found) {
                yearlySummary.add(oneSummary);
            }
        }

        return yearlySummary;
    }


    public Map<Integer, Double> getAdminYearlyBudgetTotal(List<Budget> budgetList){
        Map<Integer, Double> adminYearlyBudgetTotal = new HashMap<>();

        for (Budget budget : budgetList){
            Integer year = budget.getCreatedAt().getYear();
            Map<Integer, Double> oneYearMap = adminExpenseCalculator.calculateYearlyBudgetAmount(year, budgetList);
            boolean found = false;
                if (adminYearlyBudgetTotal.get(year)==null) {
                    adminYearlyBudgetTotal.put(year, oneYearMap.get(year));
                }
            }

        return adminYearlyBudgetTotal;
    }


    public Map<String, Object> currentMonthSummary(List<RecurringExpense> expenseList, String monthValue) {
        if(monthValue==null){
            monthValue=LocalDate.now().getMonth().name().substring(0,3);
        }
        List<Map<String, Double>> summaryItem = new ArrayList<>();
        Map<String, Object> monthlySummary = new HashMap<>();

        for (RecurringExpense expense : expenseList
                .stream()
                .sorted(Comparator.comparingDouble(RecurringExpense::getAmount).reversed()) // Sort by amount in descending order
                .limit(4) // Take the top 5
                .toList()
        ) {
            Map<String, Double> summaryItemDetail = new HashMap<>();

            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);
            int year = expense.getCreatedAt().getYear();
            String expenseName = expense.getName();
            Double amount = expense.getAmount();

            if(monthKey.equals(monthValue) && year == LocalDate.now().getYear()){
                summaryItemDetail.put(expenseName, summaryItemDetail.getOrDefault(expenseName, 0.0) + amount);
            }
            summaryItem.add(summaryItemDetail);
            monthlySummary.put(monthKey, summaryItem);
        }

        return monthlySummary; // Return the summary as a list
    }
}