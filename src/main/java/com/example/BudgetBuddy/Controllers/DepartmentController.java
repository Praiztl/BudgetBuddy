package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.DTO.DepartmentDTO;
import com.example.BudgetBuddy.Models.*;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.DepartmentService;
import com.example.BudgetBuddy.Services.OneTimeExpenseService;
import com.example.BudgetBuddy.Services.RecurringExpenseService;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import com.example.BudgetBuddy.Utilities.ExpenseSummariser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private OneTimeExpenseService oneTimeExpenseService;

    @Autowired
    private RecurringExpenseService recurringExpenseService;

    @Autowired
    private CsvUtil csvUtil;

    @GetMapping
    public ResponseEntity<List<Department>> getDepartments(){
        return departmentService.getAllDepartments();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable(name = "id")Long id){
        return departmentService.getDepartmentById(id);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Department> createDepartment(@RequestBody Department name){
        return departmentService.createDepartment(name);
    }

//    @PutMapping(path = "/{id}/update")
//    private Department updateDepartment(@PathVariable(name = "id")Integer id, DepartmentDTO updates){
//        return service.updateDepartment(id, updates);
//    }
//
    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<Department> deleteDepartment(@PathVariable(name = "id")Long id){
        return departmentService.deleteDepartment(id);
    }


    /*
    Endpoint for creating a budget within a department
     */
    @PostMapping(path = "/{id}/budgets/upload")
    public ResponseEntity<List<Budget>> read(@PathVariable(name = "id") Long id, @RequestBody MultipartFile file){

        if (csvUtil.hasCSVFormat(file)){
            try{
                return csvUtil.readBudget(file.getInputStream(), id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /*
    Endpoint for getting budgets for the department
     */
    @PostMapping(path = "/{id}/budgets")
    public ResponseEntity<List<Budget>> getBudgetsForDepartment(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(departmentService.getAllBudgets(id), HttpStatus.OK);
    }



    /*
        Department Dashboard endpoint
     */
    @GetMapping(path = "/{id}/dashboard")
    public ResponseEntity<HODDashboardCounts> getDashboard(@PathVariable(name = "id")Long id){
        HODDashboardCounts dashboard = new HODDashboardCounts(
                budgetService.getAllBudgets().size(),
                departmentService.getApprovedBudgets(id).size(),
                departmentService.getPendingBudgets(id).size(),
                departmentService.getRejectedBudgets(id).size(),
                recurringExpenseService.getRecurringExpenses().size(),
                ExpenseSummariser.currentMonthSummary(departmentService.getRecurringExpenses(id)),
                new HODExpenseChart(
                        ExpenseSummariser.getMonthlySummary(departmentService.getRecurringExpenses(id)),
                        ExpenseSummariser.getYearlySummary(departmentService.getRecurringExpenses(id))
                ),
                departmentService.getBudgetDTOs(id)
        );

        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }

}
