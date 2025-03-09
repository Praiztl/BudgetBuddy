package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;


    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private HOD hod;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecurringExpense> recurringExpenses;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OneTimeExpense> expenses;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public List<RecurringExpense> getRecurringExpenses() {
        return recurringExpenses;
    }

    public void setRecurringExpenses(List<RecurringExpense> recurringExpenses) {
        this.recurringExpenses = recurringExpenses;
    }

    public List<OneTimeExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<OneTimeExpense> expenses) {
        this.expenses = expenses;
    }

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

    public HOD getHod() {
        return hod;
    }

    public void setHod(HOD hod) {
        this.hod = hod;
    }

    public Department(String departmentName) {
    }

    public Department(Long departmentId, String departmentName) {
        this.id = departmentId;
        this.name= departmentName;
        this.createdAt = LocalDate.now();
    }


    public Department() {
    }
}
