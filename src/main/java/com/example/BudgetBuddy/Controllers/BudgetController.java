package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.BudgetDTO;
import com.example.BudgetBuddy.DTO.UpdateBudgetDTO;
import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.HOD;
import com.example.BudgetBuddy.Models.RecurringExpense;
import com.example.BudgetBuddy.Repositories.HODRepository;
import com.example.BudgetBuddy.Services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private DTOMapperService dtoMapperService;

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
    public List<BudgetDTO> getBudgets(){
        List<Budget> result =  service.getAllBudgets();
        List<BudgetDTO> response = new ArrayList<>();
        for(Budget budget : result){
            response.add(dtoMapperService.convertToBudgetDTO(budget));
        }

        return response;
    }

    @GetMapping(path = "/{id}")
    public Budget getBudget(@PathVariable(name = "id") Long id){
        return service.getBudgetById(id);
    }

    @GetMapping(path = "/{id}/view")
    public ResponseEntity<String> exportBudgetToCsv(@PathVariable Long id) {
        StringWriter writer = new StringWriter();
        csvUtil.writeBudgetToCsv(id, new PrintWriter(writer));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=budget.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(writer.toString(), headers, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/update")
    public Budget updateBudget(@PathVariable(name = "id")Long id, @RequestBody UpdateBudgetDTO updates){
        if(updates == null){
            return null;
        }
        return service.updateBudget(id, updates);
    }

    @DeleteMapping(path = "/{id}/delete")
    public String deleteBudget(@PathVariable(name = "id") Long id){
        service.deleteBudget(id);
        return "Budget %s deleted successfully.".formatted(id);
    }

    // Endpoint to approve budget
    @PutMapping(path = "/{id}/approve")
    public Budget approveBudget(@PathVariable(name = "id") Long id){
        return service.approveBudget(id);
    }

    //Endpoint to reject budget
    @PutMapping(path = "/{id}/reject")
    public Budget reject(@PathVariable(name = "id") Long id){
        return service.rejectBudget(id);
    }

    /*
    Endpoints for One Time Expenses
     */

    @PostMapping(path = "/{id}/expenses/create")
    public OneTimeExpense createOneTimeExpense(@PathVariable(name = "id") Long id, @RequestBody OneTimeExpense expense){
        return oneTimeExpenseService.createOneTimeExpense(expense, id).getBody();
    }

    @GetMapping(path = "/{id}/expenses")
    public List<OneTimeExpense> getExpensesForBudget(@PathVariable(name = "id") Long id){
        return oneTimeExpenseService.getForBudget(id).getBody();
    }


    /*
    Endpoints for Recurring Expenses
     */

    @PostMapping(path = "/{id}/recurringexpenses/create")
    public RecurringExpense createRecurringExpense(@PathVariable(name = "id") Long id, @RequestBody RecurringExpense expense){
        return recurringExpenseService.createRecurringExpense(expense, id);
    }

    @GetMapping(path = "/{id}/recurringexpenses")
    public List<RecurringExpense> getRecExpenses(@PathVariable(name = "id") Long departmentId){
        List<RecurringExpense> result =  recurringExpenseService.getRejectedExpenses();
        List<RecurringExpense> response = new ArrayList<>();

        for (RecurringExpense expense : result){
            if(Objects.equals(expense.getAssignedTo().getId(), departmentId)){
                response.add(expense);
            }
        }
        return response;
    }

}

