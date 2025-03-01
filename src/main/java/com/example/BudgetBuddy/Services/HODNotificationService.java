package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.HODNotification;
import com.example.BudgetBuddy.Repositories.HODNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HODNotificationService {
    @Autowired
    private HODNotificationRepository repository;

    public HODNotification createNotification(HODNotification notification){
        return repository.save(notification);
    }

    public List<HODNotification> getAllNotifications(){
        return repository.findAll();
    }

    public List<HODNotification> getNotificationsForHOD(Long id){
        List<HODNotification> response = new ArrayList<>();
        List<HODNotification> results = repository.findAll();
        for(HODNotification notification: results){
            response.add(notification);
        }
        return  response;
    }

    public String deleteNotificationById(Long id){
        repository.deleteById(id);

        return "Notification with ID %s successfully deleted.".formatted(id);
    }

}
