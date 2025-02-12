package com.example.BudgetBuddy.Models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {
    private Integer id;

    private String title;

    private String message;

    private Timestamp timestamp;

    public Notification() {
    }

    public Notification(Integer id, String title, String message, LocalDate date, LocalTime time) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.timestamp = new Timestamp();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
