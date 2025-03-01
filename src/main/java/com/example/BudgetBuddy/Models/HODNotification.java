package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Setter
@Getter
public class HODNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String message;

    private LocalDate date;

    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private HOD owner;

    public HODNotification() {
    }

    public HODNotification(String title, String message, HOD owner) {
        this.title = title;
        this.message = message;
        this.owner= owner;
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

    public HOD getOwner() {
        return owner;
    }

    public void setOwner(HOD owner) {
        this.owner = owner;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}


