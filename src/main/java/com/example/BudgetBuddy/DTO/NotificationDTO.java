package com.example.BudgetBuddy.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String type;
    private String message;
    private String assignedTo;
    private String from;
    private LocalDate date;
    private LocalTime time;
    private Boolean isRead;
}
