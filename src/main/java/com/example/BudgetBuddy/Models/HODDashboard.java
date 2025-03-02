package com.example.BudgetBuddy.Models;

import com.example.BudgetBuddy.DTO.BudgetDTO;

import java.util.List;
import java.util.Map;

public class HODDashboard {
    private Integer totalBudgetCount;
    private Integer approvedBudgetCount;
    private Integer pendingBudgetCount;
    private Integer rejectedBudgetCount;
    private Integer recurrentExpenseCount;

    private List<Map<String, Double>> expenseSummary;

    private HODExpenseChart expenseChart;

    private List<BudgetDTO> budgetList;

    public HODDashboard(Integer totalBudgetCount, Integer approvedBudgetCount, Integer pendingBudgetCount, Integer rejectedBudgetCount, Integer recurrentExpenseCount, List<Map<String, Double>> expenseSummary, HODExpenseChart expenseChart, List<BudgetDTO> budgetList) {
        this.totalBudgetCount = totalBudgetCount;
        this.approvedBudgetCount = approvedBudgetCount;
        this.pendingBudgetCount = pendingBudgetCount;
        this.rejectedBudgetCount = rejectedBudgetCount;
        this.recurrentExpenseCount = recurrentExpenseCount;
//        this.monthly = monthly;
        this.expenseSummary = expenseSummary;
        this.expenseChart = expenseChart;
        this.budgetList = budgetList;
    }

    public HODDashboard() {
    }
//    private List<>
}
