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
public class CreateUserDTO {

    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private String preferredCurrency;
    private User.Role role;


}
