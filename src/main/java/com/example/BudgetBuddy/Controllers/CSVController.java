package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.Budget;
import com.example.BudgetBuddy.Utilities.CsvUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/budget")
public class CSVController {

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
}

