package com.example.BudgetBuddy.SpringSecurity;

import com.example.BudgetBuddy.Models.Admin;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Repositories.AdminRepository;
import com.example.BudgetBuddy.Repositories.HODRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final HODRepository hodRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First, try to load from Admin repository
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return admin;  // Admin implements UserDetails
        }

        // If Admin not found, try to load from HOD repository
        HOD hod = hodRepository.findByEmail(email).orElse(null);
        if (hod != null) {
            return hod;  // HOD implements UserDetails
        }

        // If neither Admin nor HOD is found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
