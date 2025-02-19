package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HODResponseDTO {


    private String email;

    private String firstName;
    private String lastName;
    private User.Role role;


}
