package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "one_time_expenses")
public class OneTimeExpense {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Double amount;

    private LocalDate createdAt;

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Budget assignedTo;

    public OneTimeExpense(String name, Double amount, Budget assignedTo) {
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

    public Budget getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Budget assignedTo) {
        this.assignedTo = assignedTo;
    }

    @Override
    public String toString() {
        return name + '(' + amount + ')';
    }
}
