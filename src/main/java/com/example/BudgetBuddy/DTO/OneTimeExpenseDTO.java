package com.example.BudgetBuddy.DTO;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeExpenseDTO {
    private String name;

    private Double amount;

    private String assignedTo;

    private LocalDate createdAt;

}
