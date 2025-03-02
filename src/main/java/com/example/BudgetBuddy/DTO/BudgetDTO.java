package com.example.BudgetBuddy.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
public class BudgetDTO {
    private Long id;
    private String name;
    private LocalDate date;
    private Double amount;
    private List<String> expenses;
    private List<String> recurringExpenses;

    public BudgetDTO() {
    }

    public BudgetDTO(Long id, String name, LocalDate date, Double amount, List<String> expenses, List<String> recurringExpenses) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.expenses = expenses;
        this.recurringExpenses = recurringExpenses;
    }
}
