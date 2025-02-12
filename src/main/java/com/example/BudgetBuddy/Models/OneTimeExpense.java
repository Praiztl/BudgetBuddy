package com.example.BudgetBuddy.Models;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
public class OneTimeExpense {
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Id
    private Integer id;

    private String name;

    private Double amount;

    private Budget assignedTo;


}
