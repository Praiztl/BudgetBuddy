package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService service;

    @GetMapping
    public ResponseEntity<List<Department>> getDepartments(){
        return service.getAllDepartments();
    }

    @GetMapping(path = "/{id}")
    private ResponseEntity<Department> getDepartment(@PathVariable(name = "id")Long id){
        return service.getDepartmentById(id);
    }

    @PostMapping(path = "/create")
    private ResponseEntity<Department> createDepartment(@RequestBody Department name){
        return service.createDepartment(name);
    }

//    @PutMapping(path = "/{id}/update")
//    private Department updateDepartment(@PathVariable(name = "id")Integer id, DepartmentDTO updates){
//        return service.updateDepartment(id, updates);
//    }
//
//    @DeleteMapping(path = "/{id}/delete")
//    private String deleteDepartment(@PathVariable(name = "id")Integer id){
//        service.deleteDepartment(id);
//        return "Department %s deleted.".formatted(id);
//    }

}
