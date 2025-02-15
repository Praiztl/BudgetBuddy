package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.Models.Department;
import com.example.BudgetBuddy.Repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository repository;

    public Department createDepartment(Department dept){
        return repository.save(dept);
    }

    public Department getDepartment(Integer id){
        return repository.findById(id).orElse(null);
    }

    public List<Department> getDepartments(){
        return repository.findAll();
    }

    public Department updateDepartment(Integer id, DepartmentDTO update){
        Department dept = repository.findById(id).orElse(null);
        if(dept!=null){
            dept.setName(update.getName());
            return dept;
        }
        return null;
    }

    public void deleteDepartment(Integer id){
        repository.deleteById(id);
    }
}
