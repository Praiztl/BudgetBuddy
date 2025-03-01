package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.UpdateBudgetDTO;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.HODRepository;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.HODService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private HODService hodService;

    @PostMapping(path = "/upload")
    public ResponseEntity<List<Budget>> read(@RequestBody MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long departmentId = null;
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User currentUser = (User)authentication.getPrincipal();
            System.out.println(currentUser.getUsername());
            HOD hod = hodService.findByEmail(currentUser.getUsername()).getBody();
            System.out.println(hod);
             if (hod != null){
                 departmentId = hod.getDepartment().getId();
             }
        }

        if (csvUtil.hasCSVFormat(file)){
            try{
                return csvUtil.readBudget(file.getInputStream(), departmentId);
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
    public Budget getBudget(@PathVariable(name = "id") Long id){
        return service.getBudgetById(id);
    }

    @PutMapping(path = "/{id}/update")
    public ResponseEntity<Budget> updateBudget(@PathVariable(name = "id")Long id, @RequestBody UpdateBudgetDTO updates){
        if(updates == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(service.updateBudget(id, updates), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{id}/delete")
    public String deleteBudget(@PathVariable(name = "id") Long id){
        service.deleteBudget(id);
        return "Budget %s deleted successfully.".formatted(id);
    }


    /*
    Endpoints for One Time Expenses
     */

    @PostMapping(path = "/{id}/expenses/create")
    public ResponseEntity<OneTimeExpense> createOneTimeExpense(@PathVariable(name = "id") Long id, @RequestBody OneTimeExpense expense){
        return oneTimeExpenseService.createOneTimeExpense(expense, id);
    }

    @GetMapping(path = "/{id}/expenses")
    public ResponseEntity<List<OneTimeExpense>> getExpensesForBudget(@PathVariable(name = "id") Long id){
        return oneTimeExpenseService.getForBudget(id);
    }


    /*
    Endpoints for Recurring Expenses
     */

    @PostMapping(path = "/{id}/recurringexpenses/create")
    public RecurringExpense createRecurringExpense(@PathVariable(name = "id") Long id, @RequestBody RecurringExpense expense){
        return recurringExpenseService.createRecurringExpense(expense, id);
    }

}

