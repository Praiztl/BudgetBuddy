package com.example.BudgetBuddy.Controllers;

import com.example.BudgetBuddy.Models.Organization;
import com.example.BudgetBuddy.Services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/organizations")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/get")
    public Map<Long, String> getAllOrganizations(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "id", required = false) Long id){
        List<Organization> result = new ArrayList<>();
        Map<Long,String> response = new HashMap<>();
        if(name==null && id ==null) {
            result.addAll(organizationService.getAllOrganizations());
        }
        else if(name!=null){
            result.add(organizationService.findByName(name));
        }        else if(id!=null){
            result.add(organizationService.findById(id));
        }

        for(Organization organization: result){
            response.put(organization.getId(), organization.getName());
        }
        return response;
    }

}
