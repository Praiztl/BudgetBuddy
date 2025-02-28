package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return ResponseEntity.ok(departments);
    }

    public Department createDepartment(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }

        Department department = new Department();
        department.setName(departmentName);
        return departmentRepository.save(department);
    }

    public ResponseEntity<Department> getDepartmentById(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Updates a department name (Admin Only)
     */
    @Transactional
    public ResponseEntity<?> updateDepartment(Long departmentId, String newName) {
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admins can update departments"));
        }

        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        department.get().setName(newName);
        departmentRepository.save(department.get());

        return ResponseEntity.ok(Map.of("message", "Department updated successfully"));
    }

    public ResponseEntity<?> deleteDepartment(Long id) {
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only admins can delete departments"));
        }

        Optional<Department> department = departmentRepository.findById(id);
        if (department.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Department not found"));
        }

        departmentRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Department deleted successfully"));
    }

    /**
     * Checks if the current user is an Admin based on Spring Security roles.
     */
    private boolean isCurrentUserAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }
}
