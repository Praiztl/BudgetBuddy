package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findByUserId(String userId);
}
