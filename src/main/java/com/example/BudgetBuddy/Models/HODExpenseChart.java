package com.example.BudgetBuddy.Models;

import java.util.List;
import java.util.Map;

public class HODExpenseChart {
    private List<Map<String, Object>> monthly;
    private List<Map<String, Object>> yearly;

    public List<Map<String, Object>> getMonthly() {
        return monthly;
    }

    public void setMonthly(List<Map<String, Object>> monthly) {
        this.monthly = monthly;
    }

    public List<Map<String, Object>> getYearly() {
        return yearly;
    }

    public void setYearly(List<Map<String, Object>> yearly) {
        this.yearly = yearly;
    }

    public HODExpenseChart(List<Map<String, Object>> monthly, List<Map<String, Object>> yearly) {
        this.monthly = monthly;
        this.yearly = yearly;
    }

    public HODExpenseChart() {
    }
}
