package com.example.BudgetBuddy.Models;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class RecurringExpense extends OneTimeExpense{
    private String interval = "weekly";

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
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
