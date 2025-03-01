package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.HODNotification;
import com.example.BudgetBuddy.Services.HODNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/hod-notifications")
public class HODNotificationController {
    @Autowired
    private HODNotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<HODNotification>> getAllNotifications(){
        return new ResponseEntity<>(notificationService.getAllNotifications(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<String> deleteNotification(@PathVariable(name = "id")Long id){
        return new ResponseEntity<>(notificationService.deleteNotificationById(id), HttpStatus.OK);
    }
}
