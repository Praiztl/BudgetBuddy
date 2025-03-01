package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_expenses")
@Data
public class RecurringExpense {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Double amount;

    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Budget assignedTo;

    @Enumerated(EnumType.STRING)
    private Interval expenseInterval = Interval.Weekly;

    public Interval getExpenseInterval() {
        return expenseInterval;
    }

    public void setExpenseInterval(Interval expenseInterval) {
        this.expenseInterval = expenseInterval;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Budget getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Budget assignedTo) {
        this.assignedTo = assignedTo;
    }

    public RecurringExpense() {
    }

    public RecurringExpense(String name, Double amount, Budget assignedTo, Interval interval) {
        this.name = name;
        this.amount = amount;
        this.assignedTo = assignedTo;
        this.expenseInterval = interval;
        this.createdAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return name + '(' + amount + ')';
    }

    public enum Interval{
        Daily,
        Weekly,
        Monthly,
        Yearly
    }
}
