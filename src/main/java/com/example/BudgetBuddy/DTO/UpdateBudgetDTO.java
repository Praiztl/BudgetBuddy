package com.example.BudgetBuddy.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBudgetDTO {
    String name;

    Double amount;
}
