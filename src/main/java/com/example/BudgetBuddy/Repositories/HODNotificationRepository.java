package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.HODNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HODNotificationRepository extends JpaRepository<HODNotification, Long> {
//    public List<Notification> findByUserId(String userId);
}
