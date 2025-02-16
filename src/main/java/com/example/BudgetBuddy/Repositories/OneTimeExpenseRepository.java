package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.OneTimeExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimeExpenseRepository extends JpaRepository<OneTimeExpense, Integer> {
}
