package com.example.BudgetBuddy.Models;

import java.util.List;
import java.util.Map;

public class AdminDashboard {
    private List<Map<String,Double>> totalBudgetsByDepartment;
    private List<Map<String,Double>> totalRecurringExpensesByDepartment;

    private Integer noOfDepartments;

    private Integer totalNoOfBudgets;

    private Map<String, Integer> totalBudgetsByApprovalStatus;

    private Integer totalNoOfRecurringExpenses;

    private Map<String, Integer> totalRecurringExpensesByApprovalStatus;

    private HODExpenseChart expenseChart;

    private Map<Integer, Integer> totalApprovedBudgetsPerYear;
}
