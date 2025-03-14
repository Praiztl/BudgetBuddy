package com.example.BudgetBuddy.Repositories;



import com.example.BudgetBuddy.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository<H extends User> extends JpaRepository<User, String> {
    Optional <User> findByEmail(String email);
    boolean existsByEmail(String email);
}

