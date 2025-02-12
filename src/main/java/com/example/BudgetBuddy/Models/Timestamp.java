package com.example.BudgetBuddy.Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class Timestamp {
    private LocalDate date;
    private LocalTime time;

    public Timestamp() {
        this.date = LocalDate.now(ZoneId.systemDefault());
        this.time = LocalTime.now(ZoneId.systemDefault());
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

    @Override
    public String toString() {
        return "Timestamp{" +
                "date=" + date +
                ", time=" + time +
                '}';
    }
}
