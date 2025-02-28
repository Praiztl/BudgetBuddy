package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.Data;

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

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Budget assignedTo;

    private Interval interval = Interval.Weekly;

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
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
        this.interval = interval;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "RecurringExpense{" +
                "interval='" + interval + '\'' +
                '}';
    }

    public enum Interval{
        Weekly,
        Monthly
    }
}
