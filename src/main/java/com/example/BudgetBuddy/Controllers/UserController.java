package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.CreateUserDTO;
import com.example.BudgetBuddy.DTO.UserResponseDTO;
import com.example.BudgetBuddy.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    /**
     * Retrieve all users.
     *
     * @return List of UserResponseDTO
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieve a user by ID.
     *
     * @param id User ID
     * @return UserResponseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Create a new user.
     *
     * @param createUserDTO Data Transfer Object for creating user
     * @return Created UserResponseDTO
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    /**
     * Update an existing user by ID.
     *
     * @param id             User ID
     * @param createUserDTO Data Transfer Object for updating user
     * @return Updated UserResponseDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody CreateUserDTO createUserDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, createUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by ID.
     *
     * @param id User ID
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
