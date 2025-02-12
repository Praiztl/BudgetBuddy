package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, String> {
}
