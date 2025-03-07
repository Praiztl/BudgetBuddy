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

    @ManyToOne
    @JoinColumn(name = "assignedTo_id")
    private Department assignedTo;

    private String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

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

    public Notification(String type, String message, Department department, String from) {
        this.type = type;
        this.message = message;
        this.assignedTo = department;
        this.sender = from;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public Notification(String type, String message, String from) {
        this.type = type;
        this.message = message;
        this.sender = from;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
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

    public Department getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Department assignedTo) {
        this.assignedTo = assignedTo;
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
