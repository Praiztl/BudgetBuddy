package com.example.BudgetBuddy.Models;

import java.util.List;
import java.util.Map;

public class HODExpenseChart {
    private List<Map<String, Double>> monthly;
    private List<Map<Integer, Double>> yearly;

    public List<Map<String, Double>> getMonthly() {
        return monthly;
    }

    public void setMonthly(List<Map<String, Double>> monthly) {
        this.monthly = monthly;
    }

    public List<Map<Integer, Double>> getYearly() {
        return yearly;
    }

    public void setYearly(List<Map<Integer, Double>> yearly) {
        this.yearly = yearly;
    }

    public HODExpenseChart(List<Map<String, Double>> monthly, List<Map<Integer, Double>> yearly) {
        this.monthly = monthly;
        this.yearly = yearly;
    }

    public HODExpenseChart() {
    }
}
