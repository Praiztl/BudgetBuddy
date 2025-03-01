package com.example.BudgetBuddy.Utilities;


import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Services.BudgetService;
import com.example.BudgetBuddy.Services.DepartmentService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvUtil {
    @Autowired
    private BudgetService budgetService;

    @Autowired
    private DepartmentService departmentService;

    public String TYPE = "text/csv";

    public boolean hasCSVFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }


    public ResponseEntity<List<Budget>> readBudget(InputStream is, Long departmentId) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Budget> budgets= new ArrayList<Budget>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Budget budget = new Budget(csvRecord.get("Name"), Double.parseDouble(csvRecord.get("Amount")));
                budget.setDepartment(departmentService.getDepartmentById(departmentId).getBody());
                budgets.add(budget);
            }

            for(Budget budget: budgets){
                budgetService.createBudget(budget);
            }
            return new ResponseEntity<>(budgets, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}