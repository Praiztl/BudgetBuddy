package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Department> departments;

    @OneToOne(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Organization(String name) {
        this.name = name;
    }

    public Organization() {
    }
}
