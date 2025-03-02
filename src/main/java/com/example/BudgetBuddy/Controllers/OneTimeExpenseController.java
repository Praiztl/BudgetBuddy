package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.OneTimeExpenseDTO;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Services.DTOMapperService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/onetimeexpenses")
public class OneTimeExpenseController {
    @Autowired
    private OneTimeExpenseService service;

    @Autowired
    private DTOMapperService dtoMapperService;

    @PostMapping(path = "/create")
    public ResponseEntity<OneTimeExpense> createOneTimeExpense(@RequestBody OneTimeExpense expense, @RequestBody Long budgetId){
        return service.createOneTimeExpense(expense, budgetId);
    }

    /*
    Endpoint that retrieves all one-time expenses
     */
    @GetMapping
    public List<OneTimeExpenseDTO> getOneTimeExpenses(){
        List<OneTimeExpense> result = service.getOneTimeExpenses();
        List<OneTimeExpenseDTO> response = new ArrayList<>();

        for(OneTimeExpense expense : result){
            response.add(dtoMapperService.convertToExpenseDTO(expense));
        }
        return response;
    }

    @GetMapping(path = "/{id}")
    public OneTimeExpense getOneTimeExpense(@PathVariable(name = "id") Integer id){
        return service.getOneTimeExpense(id);
    }

    @DeleteMapping(path = "/{id}/delete")
    public String deleteOneTimeExpense(@PathVariable(name = "id")Integer id){
        service.deleteExpense(id);
        return "One time expense %s deleted.".formatted(id);
    }
}
