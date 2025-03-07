package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.RecurringExpense;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private Integer id;
    private String name;
    private Double amount;
    private String assignedTo;
    private String expenseInterval;
    private String approvalStatus;
}
