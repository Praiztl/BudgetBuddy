package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;

    private String message;

    private String departmentName;

    private LocalDate date;

    private LocalTime time;

    private Boolean isRead = false;


    public enum NotificationType{
        BudgetSubmitted,
        BudgetApproved,
        BudgetRejected,
        RecurringExpenseSubmitted,
    }

    public Notification() {
    }

    public Notification(String type, String message, String departmentName) {
        this.type = type;
        this.message = message;
        this.departmentName = departmentName;
    }

    public Notification(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
