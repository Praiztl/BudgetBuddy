package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Organization;
import com.example.BudgetBuddy.Repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations(){
        return organizationRepository.findAll();
    }

    public Organization findByName(String name){
        return organizationRepository.findByName(name);
    }

    public Organization findById(Long id){
        return organizationRepository.findById(id).orElse(new Organization());
    }
}
