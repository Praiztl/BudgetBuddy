package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.HOD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HODRepository extends JpaRepository<HOD, String> {
    Optional<HOD> findByEmail(@Param("email") String email);
}
