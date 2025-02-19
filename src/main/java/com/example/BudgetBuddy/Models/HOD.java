package com.example.BudgetBuddy.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hods")
public class HOD implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Ensure column mapping
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(unique = true)
    private String otpCode;

    private Boolean isOtpVerified = false;

    @Enumerated(EnumType.STRING)
    private HOD.Role role; // Add role field to differentiate between user roles

    public enum Role {
        HOD, // HOD role
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    @Override
    public String getUsername() {
        return this.email; // Using email as the username for login
    }

    @Override
    public String getPassword() {
        return this.password; // Return the encoded password
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Adjust based on your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Adjust based on your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Adjust based on your logic
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // Whether the account is enabled or not
    }
}
