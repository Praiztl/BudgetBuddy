package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OneTimeExpense {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Budget assignedTo;

    @Override
    public String toString() {
        return "OneTimeExpense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", assignedTo=" + assignedTo +
                '}';
    }
}
