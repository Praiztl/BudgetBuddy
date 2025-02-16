package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public Notification createNotification(Notification notification){
        return repository.save(notification);
    }

    public List<Notification> getNotificationsForUser(String userId){
        return repository.findByUserId(userId);
    }

}
