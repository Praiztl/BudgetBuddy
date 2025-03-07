package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.NotificationDTO;
import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Services.DTOMapperService;
import com.example.BudgetBuddy.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DTOMapperService dtoMapperService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(){
        List<Notification> response = notificationService.getAllNotifications();
        List<NotificationDTO> result = new ArrayList<>();
        for (Notification notification: response){
            result.add(dtoMapperService.convertToNotificationDTO(notification));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/view")
    public ResponseEntity<?> viewNotification(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(dtoMapperService.convertToNotificationDTO(notificationService.readNotification(id)), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<String> deleteNotification(@PathVariable(name = "id") Long id){
        notificationService.deleteNotificationById(id);
        return new ResponseEntity<>("Notification %s deleted successfully".formatted(id), HttpStatus.OK);
    }

}
