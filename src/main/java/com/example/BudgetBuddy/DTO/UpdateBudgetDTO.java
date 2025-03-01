package com.example.BudgetBuddy.DTO;

import lombok.*;

import javax.annotation.Nullable;

@Data
public class UpdateBudgetDTO {
    private String name;

    private Double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UpdateBudgetDTO() {
    }

    public UpdateBudgetDTO(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public UpdateBudgetDTO(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateBudgetDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
