package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.OTPCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPCode, Long> {
    Optional<OTPCode> findByEmailAndOtp(String email, String otp);
    Optional<OTPCode> findByEmail(String email);
    void deleteByEmail(String email);
}
