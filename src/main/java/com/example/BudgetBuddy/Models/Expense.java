package com.example.BudgetBuddy.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    Integer id;

    String name;

    Double amount;

    LocalDate createdAt;

    private Department assignedTo;

}
