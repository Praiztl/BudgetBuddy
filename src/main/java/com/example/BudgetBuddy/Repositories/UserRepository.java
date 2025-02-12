package com.example.BudgetBuddy.Repositories;



import com.example.BudgetBuddy.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional <User> findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
}

