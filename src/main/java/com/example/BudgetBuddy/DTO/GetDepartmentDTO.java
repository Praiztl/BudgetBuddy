package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.HOD;
import lombok.*;

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

    private Long hod;
}
