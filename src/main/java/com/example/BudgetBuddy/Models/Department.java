package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private HOD hod;


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
