package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.OTPCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPCode, Long> {
    Optional<OTPCode> findByEmail(String email);
}
