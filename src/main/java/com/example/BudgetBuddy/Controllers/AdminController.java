package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/notifications")
    public ResponseEntity<List<Notification>> getNotificationsForaAdmin(){
        return new ResponseEntity<>(notificationService.getNotificationsForAdmin(), HttpStatus.OK);
    }
}
