package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/budgets")
public class BudgetController {

    @Autowired
    private BudgetService service;

    @PostMapping(path = "/upload")
    public List<Budget> read(@RequestBody MultipartFile file){
        if (CsvUtil.hasCSVFormat(file)){
            try{
                return CsvUtil.readBudget(file.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @GetMapping
    public List<Budget> getBudgets(){
        return service.getAllBudgets();
    }
}

