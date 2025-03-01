package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.RecurringExpense;

import java.time.LocalDate;
import java.util.*;

public class ExpenseSummariser {

    public static List<Map<String, Double>> getMonthlySummary(List<RecurringExpense> expenseList) {
        Map<String, Double> monthlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);
            monthlySummary.put(monthKey, monthlySummary.getOrDefault(monthKey, 0.0) + expense.getAmount());
        }

        return List.of(monthlySummary); // Return the summary as a list
    }

    public static List<Map<Integer, Double>> getYearlySummary(List<RecurringExpense> expenseList) {
        Map<Integer, Double> yearlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            Integer yearKey = expense.getCreatedAt().getYear();
            yearlySummary.put(yearKey, yearlySummary.getOrDefault(yearKey, 0.0) + expense.getAmount());
        }

        return List.of(yearlySummary); // Return the summary as a list
    }

    public static List<Map<String, Double>> currentMonthSummary(List<RecurringExpense> expenseList) {
        Map<String, Double> monthlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);
            Integer year = expense.getCreatedAt().getYear();
            if(monthKey.equals(LocalDate.now().getMonth().name().substring(0, 3)) && year == LocalDate.now().getYear()){
            monthlySummary.put(expense.getName(), monthlySummary.getOrDefault(monthKey, 0.0) + expense.getAmount());
            }
        }

        return List.of(monthlySummary); // Return the summary as a list
    }
}