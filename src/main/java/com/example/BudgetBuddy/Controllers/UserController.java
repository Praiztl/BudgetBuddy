package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.HODResponseDTO;
import com.example.BudgetBuddy.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    /**
     * Retrieve all users (both Admin and HOD-Pending).
     *
     * @return List of UserResponseDTO
     */


    /**
     * Retrieve a user by ID (Admin or HOD-Pending).
     *
     * @param id User ID
     * @return UserResponseDTO
     */


    /**
     * Delete a user by ID (Admin or HOD).
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
