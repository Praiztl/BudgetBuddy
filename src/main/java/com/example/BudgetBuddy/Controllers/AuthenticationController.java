package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.CreateUserDTO;
import com.example.BudgetBuddy.DTO.LoginRequest;
import com.example.BudgetBuddy.DTO.UserResponseDTO;
import com.example.BudgetBuddy.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody CreateUserDTO createUserDTO) {
        return authenticationService.createUser(createUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }
}
