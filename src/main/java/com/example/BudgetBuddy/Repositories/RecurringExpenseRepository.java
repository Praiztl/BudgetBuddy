package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.RecurringExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Integer> {
}
