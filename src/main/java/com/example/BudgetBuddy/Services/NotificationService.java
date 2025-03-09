package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.NotificationDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Models.Notification;
import com.example.BudgetBuddy.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DTOMapperService dtoMapperService;

    public Notification createNotification(Notification notification){
        notification.setDate(LocalDate.now());
        notification.setTime(LocalTime.now());
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications(){
        return notificationRepository.findAll();
    }


    public List<NotificationDTO> getNotificationsByDepartment(Department department){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> result = new ArrayList<>();

        for(Notification notification : notifications){
            if((notification.getAssignedTo()!= null && notification.getAssignedTo().equals(department))){
                result.add(dtoMapperService.convertToNotificationDTO(notification));
            }
        }
        return result;
    }

    public List<NotificationDTO> getNotificationsForAdmin(Long orgId){
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> result = new ArrayList<>();

        for(Notification notification : notifications){
            if(notification.getAssignedTo()==null && notification.getOrganization().getId().equals(orgId)){
                result.add(dtoMapperService.convertToNotificationDTO(notification));
            }
        }
        return result;
    }


    public Notification readNotification(Long notificationId) throws Exception {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new Exception("Notification not found."));
        if(!notification.getRead()){
            notification.setRead(true);
        }
        notificationRepository.save(notification);
        return notification;
    }

    public String deleteNotificationById(Long notificationId){
        notificationRepository.deleteById(notificationId);
        return "Notification % deleted successfully.".formatted(notificationId);
    }
}
