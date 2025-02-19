package com.example.BudgetBuddy.Validation;

import com.example.BudgetBuddy.DTO.AdminRegistrationDTO;
import com.example.BudgetBuddy.DTO.HODRegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto instanceof AdminRegistrationDTO adminDto) {
            return isPasswordMatching(adminDto.getPassword(), adminDto.getConfirmPassword());
        } else if (dto instanceof HODRegistrationDTO hodDto) {
            return isPasswordMatching(hodDto.getPassword(), hodDto.getConfirmPassword());
        }
        return false;
    }

    private boolean isPasswordMatching(String password, String confirmPassword) {
        return password != null && confirmPassword != null && password.equals(confirmPassword);
    }
}
