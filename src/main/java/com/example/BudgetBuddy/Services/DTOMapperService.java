package com.example.BudgetBuddy.Services;


import com.example.BudgetBuddy.DTO.CreateUserDTO;
import com.example.BudgetBuddy.DTO.UserResponseDTO;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.User;
import com.example.BudgetBuddy.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DTOMapperService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    // Convert User entity to UserResponseDTO
    public UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .preferredCurrency(user.getPreferredCurrency())
                .build();
    }

    // Convert CreateUserDTO to User entity
    public User convertToUserEntity(CreateUserDTO createUserDTO) {
        return User.builder()
                .email(createUserDTO.getEmail())
                .name(createUserDTO.getName())
                .role(createUserDTO.getRole())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .phoneNumber(createUserDTO.getPhoneNumber())
                .profileImageUrl(createUserDTO.getProfileImageUrl())
                .preferredCurrency(createUserDTO.getPreferredCurrency())
                .build();
    }
}
