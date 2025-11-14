package com.ZerodaySolution.Billing.controller;


import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.dto.IncomeDTO;
import com.ZerodaySolution.Billing.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
public class IncomeController {

    public final IncomeService service;

    @PostMapping
    public ResponseEntity<IncomeDTO> create(@RequestBody IncomeDTO dto){
        IncomeDTO saved =    service.createIncome(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);


    }


    @GetMapping("/all")
    public ResponseEntity<List<IncomeDTO>> getMonthlyExpenses(){

        List<IncomeDTO> list = service.getAllIncomesForCurrentMonthForCurrentUser();

        return ResponseEntity.ok(list);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<  Void> delete(@PathVariable Long id){
        service.deleteIncome(id);
        return ResponseEntity.noContent().build();

    }
}
