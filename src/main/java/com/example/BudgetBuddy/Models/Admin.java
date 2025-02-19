package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "admins")
public class Admin implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private boolean enabled; // Adjust depending on your needs

    @Enumerated(EnumType.STRING)
    private Role role; // Add role field to differentiate between user roles

    public enum Role {
        ADMIN, // Admin role
        // Add other roles if necessary, such as HOD
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public String getUsername() {
        return this.email; // Using email as username for authentication
    }

    @Override
    public String getPassword() {
        return this.password; // Return encoded password
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // Whether the account is enabled or not
    }
}
