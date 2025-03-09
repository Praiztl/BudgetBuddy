package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.Budget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BudgetDTO {
    private Long id;
    private String name;
    private LocalDate date;
    private Double amount;
    private String departmentName;
    private String organizationName;
    private Budget.Status approvalStatus;
    private List<String> oneTimeExpenses;
    private List<String> recurringExpenses;

    public BudgetDTO() {
    }

    public BudgetDTO(Long id, String name, LocalDate date, Double amount, Budget.Status status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.approvalStatus = status;
    }
}
