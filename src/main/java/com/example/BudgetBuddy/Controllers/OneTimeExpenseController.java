package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/onetimeexpenses")
public class OneTimeExpenseController {
    @Autowired
    private OneTimeExpenseService service;

    @PostMapping(path = "/create")
    public OneTimeExpense createOneTimeExpense(OneTimeExpense expense){
        return service.createOneTimeExpense(expense);
    }

    /*
    Endpoint that retrieves all one-time expenses
     */
    @GetMapping
    public List<OneTimeExpense> getOneTimeExpenses(){
        return service.getOneTimeExpenses();
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
