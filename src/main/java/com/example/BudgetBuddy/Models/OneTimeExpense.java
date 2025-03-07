package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "one_time_expenses")
public class OneTimeExpense extends Expense {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Double amount;

    private LocalDate createdAt;


    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    private String budgetName;

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Department assignedTo;

    public OneTimeExpense(String name, Double amount, Department assignedTo) {
        this.name = name;
        this.amount = amount;
        this.assignedTo = assignedTo;
        this.createdAt = LocalDate.now();
    }

    public OneTimeExpense() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Department getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Department assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return name + '(' + amount + ')';
    }
}
