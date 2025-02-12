package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.CreateUserDTO;
import com.example.BudgetBuddy.DTO.UserResponseDTO;
import com.example.BudgetBuddy.Exceptions.UserNotFoundException;
import com.example.BudgetBuddy.Models.User;
import com.example.BudgetBuddy.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DTOMapperService dtoMapperService;

    /**
     * Retrieve all users and map them to UserResponseDTO.
     *
     * @return List of UserResponseDTO
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(dtoMapperService::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a user by ID and map to UserResponseDTO.
     *
     * @param id User ID
     * @return UserResponseDTO
     */
    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return dtoMapperService.convertToUserResponseDTO(user);
    }

    /**
     * Create a new user from CreateUserDTO and return UserResponseDTO.
     *
     * @param createUserDTO Data Transfer Object for user creation
     * @return UserResponseDTO
     */
    public ResponseEntity<UserResponseDTO> createUser(CreateUserDTO createUserDTO) {
        User user = dtoMapperService.convertToUserEntity(createUserDTO);
        User createdUser = userRepository.save(user);
        UserResponseDTO userResponseDTO = dtoMapperService.convertToUserResponseDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    /**
     * Update an existing user by ID using CreateUserDTO and return updated UserResponseDTO.
     *
     * @param id             User ID
     * @param createUserDTO Data Transfer Object for updating user
     * @return Updated UserResponseDTO
     */
    public UserResponseDTO updateUser(String id, CreateUserDTO createUserDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Convert updated user details
        User updatedUser = dtoMapperService.convertToUserEntity(createUserDTO);
        updateUserFields(existingUser, updatedUser);

        User savedUser = userRepository.save(existingUser);
        return dtoMapperService.convertToUserResponseDTO(savedUser);
    }

    /**
     * Delete a user by ID.
     *
     * @param id User ID
     */
    public void deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    /**
     * Update fields of an existing user with the values from an updated user.
     *
     * @param existingUser The existing user to be updated
     * @param updatedUser  The user containing updated values
     */
    private void updateUserFields(User existingUser, User updatedUser) {
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setProfileImageUrl(updatedUser.getProfileImageUrl());
        existingUser.setPreferredCurrency(updatedUser.getPreferredCurrency());
        existingUser.setCreatedAt(updatedUser.getCreatedAt()); // Update timestamps if needed
        existingUser.setUpdatedAt(updatedUser.getUpdatedAt()); // Update timestamps if needed
    }
}
