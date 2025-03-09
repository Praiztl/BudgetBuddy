package com.example.BudgetBuddy.Repositories;

import com.example.BudgetBuddy.Models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    public Organization findByName(String organizationName);
}
