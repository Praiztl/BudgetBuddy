package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.OTPCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPCode, Long> {
    Optional<OTPCode> findByEmailAndOtp(String email, String otp);
    Optional<OTPCode> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM OTPCode o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);
}
