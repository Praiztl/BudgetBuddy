package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.HOD;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDepartmentDTO {
    private Long id;

    private String name;

    private List<BudgetDTO> budgets;

    private String hod;

    private LocalDate createdAt;

    private String organizationName;

    private List<OneTimeExpenseDTO> oneTimeExpenses;

    private List<RecExpenseDTO> recurringExpenses;
}
