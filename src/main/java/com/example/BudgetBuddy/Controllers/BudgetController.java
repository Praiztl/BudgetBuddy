package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.UpdateBudgetDTO;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/budgets")
public class BudgetController {

    @Autowired
    private BudgetService service;

    @Autowired
    private CsvUtil csvUtil;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @PostMapping(path = "/upload")
    public List<Budget> read(@RequestBody MultipartFile file){
        if (csvUtil.hasCSVFormat(file)){
            try{
                return csvUtil.readBudget(file.getInputStream());
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

    @GetMapping(path = "/{id}")
    public Budget getBudget(@PathVariable(name = "id") String id){
        return service.getBudgetById(id);
    }

    @PutMapping(path = "/{id}/update")
    public Budget updateBudget(@PathVariable(name = "id")String id, UpdateBudgetDTO updates){
        return service.updateBudget(id, updates);
    }

    @DeleteMapping(path = "/{id}/delete")
    public String deleteBudget(@PathVariable(name = "id") String id){
        service.deleteBudget(id);
        return "Budget %s deleted successfully.".formatted(id);
    }


    /*
    Endpoints for One Time Expenses
     */

    @PostMapping(path = "/{id}/expenses/create")
    public ResponseEntity<OneTimeExpense> createOneTimeExpense(@PathVariable(name = "id") String id, @RequestBody OneTimeExpense expense){
        return oneTimeExpenseService.createOneTimeExpense(expense, id);
    }

    @GetMapping(path = "/{id}/expenses")
    public ResponseEntity<List<OneTimeExpense>> getExpensesForBudget(@PathVariable(name = "id") String id){
        return oneTimeExpenseService.getForBudget(id);
    }
}

