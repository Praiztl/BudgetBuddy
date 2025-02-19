package com.example.BudgetBuddy.DTO;

import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Validation.PasswordMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordMatch(message = "Passwords must match")
public class HODRegistrationDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword; // For password confirmation

    @NotNull(message = "Department ID is required")
    private Long departmentId; // Department selected by HOD during registration

    @NotBlank(message = "OTP is required")
    private String otp;

    public Department getDepartment() {
        return new Department(departmentId, "Department Name");
    }

    // Custom validation to ensure passwords match
    public boolean isPasswordMatching() {
        return this.password != null && this.password.equals(this.confirmPassword);
    }
}
