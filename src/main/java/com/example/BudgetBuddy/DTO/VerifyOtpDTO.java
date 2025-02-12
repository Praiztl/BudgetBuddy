package com.example.BudgetBuddy.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpDTO {
    private String email;
    private String otp;

}
