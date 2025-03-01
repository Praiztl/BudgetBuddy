package com.example.BudgetBuddy.Utilities;

import com.example.BudgetBuddy.Models.RecurringExpense;

import java.time.LocalDate;
import java.util.*;

public class ExpenseSummariser {

    public static List<Map<String, Double>> getMonthlySummary(List<RecurringExpense> expenseList) {
        Map<String, Double> monthlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);

            /*
            Calculating total value of recurring expense per month, based on the set interval
             */
            if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                expense.setAmount(expense.getAmount()*4);
            } else if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)) {
                expense.setAmount(expense.getAmount()* expense.getCreatedAt().getMonth().length(expense.getCreatedAt().getYear()%4==0));
            }

            /*
            Adding the calculated value to the resulting map
             */
            monthlySummary.put(monthKey, monthlySummary.getOrDefault(monthKey, 0.0) + expense.getAmount());
        }

        return List.of(monthlySummary); // Return the summary as a list
    }

    public static List<Map<Integer, Double>> getYearlySummary(List<RecurringExpense> expenseList) {
        Map<Integer, Double> yearlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            Integer yearKey = expense.getCreatedAt().getYear();

            /*
            Calculating value of recurring expenses per year, based on set interval
             */
            if(expense.getExpenseInterval().equals(RecurringExpense.Interval.Daily)){
                if(expense.getCreatedAt().getYear()%4==0){
                    expense.setAmount(expense.getAmount() * 366);
                } else{
                    expense.setAmount(expense.getAmount() * 365);
                }
            }

            else if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Weekly)){
                expense.setAmount(expense.getAmount() * 52);
            }

            else if (expense.getExpenseInterval().equals(RecurringExpense.Interval.Monthly)){
                expense.setAmount(expense.getAmount() * 12);
            }

            /*
            Adding the calculated value to the resulting map
             */
            yearlySummary.put(yearKey, yearlySummary.getOrDefault(yearKey, 0.0) + expense.getAmount());
        }

        return List.of(yearlySummary); // Return the summary as a list
    }

    public static List<Map<String, Double>> currentMonthSummary(List<RecurringExpense> expenseList) {
        Map<String, Double> monthlySummary = new HashMap<>(); // Initialize the map

        for (RecurringExpense expense : expenseList) {
            String monthKey = expense.getCreatedAt().getMonth().name().substring(0, 3);
            int year = expense.getCreatedAt().getYear();
            if(monthKey.equals(LocalDate.now().getMonth().name().substring(0, 3)) && year == LocalDate.now().getYear()){
            monthlySummary.put(expense.getName(), monthlySummary.getOrDefault(monthKey, 0.0) + expense.getAmount());
            }
        }

        return List.of(monthlySummary); // Return the summary as a list
    }
}