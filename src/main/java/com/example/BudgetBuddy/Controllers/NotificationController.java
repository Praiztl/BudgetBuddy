package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(){
        List<Notification> response = notificationService.getAllNotifications();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<String> deleteNotification(@PathVariable(name = "id") Long id){
        notificationService.deleteNotificationById(id);
        return new ResponseEntity<>("Notification %s deleted successfully".formatted(id), HttpStatus.OK);
    }

}
