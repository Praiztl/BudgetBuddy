package com.example.BudgetBuddy.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String message;

    private LocalDate date;

    private LocalTime time;

    private Admin admin;

    private HOD hod;

//    private String userId;


    public Notification() {
    }

    public Notification(Long id, String title, String message, LocalDate date, LocalTime time) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.time = LocalTime.now();
        this.date = LocalDate.now();
//        this.userId = this.owner.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    }


