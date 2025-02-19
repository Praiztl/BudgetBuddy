package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponseDTO {


    private String email;
    private User.Role role;


}
