package com.example.BudgetBuddy.DTO;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeExpenseDTO {
    private Integer id;

    private String name;

    private Double amount;

    private Long assignedTo;

    private LocalDate createdAt;

    private String approvalStatus;

    private String budgetName;

}
