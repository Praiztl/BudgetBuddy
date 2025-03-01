package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Budget {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String name;

    private LocalDate createdAt;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    private List<OneTimeExpense> expenses;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    private List<RecurringExpense> recurringExpenses;

    @Enumerated(EnumType.STRING)
    private Status status = Status.Pending;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<OneTimeExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<OneTimeExpense> expenses) {
        this.expenses = expenses;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<RecurringExpense> getRecurringExpenses() {
        return recurringExpenses;
    }

    public Budget() {
    }

    public Budget(String name, Double amount) {
        this.name = name;
        this.amount = amount;
        this.createdAt = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", amount=" + amount +
                ", expenses=" + expenses +
                ", status='" + status + '\'' +
                '}';
    }


    public enum Status{
        Pending,
        Approved,
        Rejected
    }
}
