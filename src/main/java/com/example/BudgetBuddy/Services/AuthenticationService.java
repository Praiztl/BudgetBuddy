package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.CreateUserDTO;
import com.example.BudgetBuddy.DTO.LoginRequest;
import com.example.BudgetBuddy.DTO.UserResponseDTO;
import com.example.BudgetBuddy.Models.User;
import com.example.BudgetBuddy.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final DTOMapperService dtoMapperService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public ResponseEntity<UserResponseDTO> createUser(CreateUserDTO createUserDTO) {
        // Find user by email
        User existingUser = userRepository.findByEmail(createUserDTO.getEmail())
                .orElse(null);
        if (existingUser != null) {
            // User already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // User does not exist, proceed with registration
            User user = dtoMapperService.convertToUserEntity(createUserDTO);
//            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
            User createdUser = userRepository.save(user);
            UserResponseDTO userResponseDTO = dtoMapperService.convertToUserResponseDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
        }
    }

    public ResponseEntity<String> login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(jwtToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }
}
