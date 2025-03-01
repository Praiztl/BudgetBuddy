package com.example.BudgetBuddy.DTO;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public class BudgetDTO {
    private String name;
    private LocalDate date;
    private Double amount;
    private List<String> expenses;
    private List<String> recurringExpenses;
}
