package com.example.BudgetBuddy.Services;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Models.OneTimeExpense;
import com.example.BudgetBuddy.Repositories.OneTimeExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OneTimeExpenseService {
    @Autowired
    private OneTimeExpenseRepository repository;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DepartmentService departmentService;

    public ResponseEntity<OneTimeExpense> createOneTimeExpense(OneTimeExpense expense, Long departmentId){
        if (expense.getAssignedTo() == null) {
            expense.setAssignedTo(departmentService.getDepartmentById(departmentId).getBody());
            expense.setCreatedAt(LocalDate.now());
            expense.setBudgetName((departmentService.getApprovedBudgets(departmentId).get(0).getName()));
            OneTimeExpense response = repository.save(expense);
            return new ResponseEntity<OneTimeExpense>(response, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /*
    To retrieve a single one-time expense
     */
    public OneTimeExpense getOneTimeExpense(Integer id){
        return repository.findById(id).orElse(null);
    }

    /*
    To retrieve all one-time expenses
     */
    public List<OneTimeExpense> getOneTimeExpenses(){
        return repository.findAll();
    }

    public ResponseEntity<List<OneTimeExpense>> getForDepartment(Long departmentId){
        List<OneTimeExpense> expenses = new ArrayList<>();
        for (OneTimeExpense expense: repository.findAll()){
            if(expense.getAssignedTo().getId().equals(departmentId)){
                expenses.add(expense);
            }
        }
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    public List<OneTimeExpense> getForOrganization(Long orgId){
        List<OneTimeExpense> orgExpenses = new ArrayList<>();
        for(OneTimeExpense expense: getOneTimeExpenses()){
            if(expense.getAssignedTo().getOrganization().getId().equals(orgId)){
                orgExpenses.add(expense);
            }
        }
        return orgExpenses;
    }

    public void deleteExpense(Integer id){
        repository.deleteById(id);
    }

    /*
    It is not expected that users will edit expenses
     */

}
