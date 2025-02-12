package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {


    private String email;
    private String name;
    private String phoneNumber;
    private String profileImageUrl;
    private String preferredCurrency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User.Role role;


}
