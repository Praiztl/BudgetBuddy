package com.example.BudgetBuddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTPCodeDTO {
    private String email;
    private String otp; // OTP sent to the user
}
