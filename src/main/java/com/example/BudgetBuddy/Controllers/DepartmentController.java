package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Services.DepartmentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Get all departments (Accessible by Anyone)
     */
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     * Create one or more departments (Only Admin)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createDepartments(@RequestBody DepartmentDTO departmentsDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Only admins can create departments.");
        }

        if (departmentsDTO.getDepartments() == null || departmentsDTO.getDepartments().isEmpty()) {
            return ResponseEntity.badRequest().body("Departments list cannot be empty.");
        }

        List<Department> createdDepartments = new ArrayList<>();

        for (String department : departmentsDTO.getDepartments()) {
            Department savedDepartment = departmentService.createDepartment(department);
            createdDepartments.add(savedDepartment);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartments);
    }

    /**
     * Get department by ID (Accessible by Anyone)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    /**
     * Delete a department (Only Admin)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        // Get authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract user roles
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied: Only admins can delete departments.");
        }

        return departmentService.deleteDepartment(id);
    }
}
