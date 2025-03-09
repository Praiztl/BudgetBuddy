package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatch(message = "Passwords must match")
public class AdminRegistrationDTO {


    @Email(message = "Please provide a valid email address")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Please confirm your password")
    private String confirmPassword; // This is for password confirmation during registration

    @NotNull(message = "Please enter a name for your organization")
    private String organizationName;

    // Custom validation to ensure passwords match
    public boolean isPasswordMatching() {
        return this.password != null && this.password.equals(this.confirmPassword);
    }
}
