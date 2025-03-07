package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.RecurringExpense;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecExpenseDTO {
    private Integer id;

    private String name;

    private Double amount;

    private String assignedTo;

    @Enumerated(EnumType.STRING)
    private RecurringExpense.Interval expenseInterval;

    @Enumerated(EnumType.STRING)
    private RecurringExpense.Status approvalStatus;
}
