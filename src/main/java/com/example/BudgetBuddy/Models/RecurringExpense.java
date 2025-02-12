package com.example.BudgetBuddy.Models;

public class RecurringExpense extends OneTimeExpense{
    private String interval = "weekly";

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public RecurringExpense() {
    }

    public RecurringExpense(String interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "RecurringExpense{" +
                "interval='" + interval + '\'' +
                '}';
    }
}
