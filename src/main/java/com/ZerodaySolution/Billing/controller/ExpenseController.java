package com.ZerodaySolution.Billing.controller;

import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {


    private final ExpenseService service;


    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody ExpenseDTO dto){
        ExpenseDTO saved =    service.createExpense(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getMonthlyExpenses(){
       List< ExpenseDTO> list = service.getAllExpensesForCurrentMonthForCurrentUser();

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<  Void> delete(@PathVariable Long id){
        service.deleteExpense(id);
        return ResponseEntity.noContent().build();

    }
}
