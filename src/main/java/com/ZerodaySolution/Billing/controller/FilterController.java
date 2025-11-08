package com.ZerodaySolution.Billing.controller;


import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.dto.FilterDTO;
import com.ZerodaySolution.Billing.dto.IncomeDTO;
import com.ZerodaySolution.Billing.services.ExpenseService;
import com.ZerodaySolution.Billing.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class FilterController {


    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> search(@RequestBody FilterDTO filterDTO){
        LocalDate startDate = filterDTO.getStartDate() !=  null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() !=  null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() :  "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        if("income".equalsIgnoreCase(filterDTO.getType())){
           List<IncomeDTO> listIncomes = incomeService.filterIncome(startDate,endDate,keyword,sort);
           return ResponseEntity.ok(listIncomes);
        } else if ("expense".equalsIgnoreCase(filterDTO.getType())) {

            List<ExpenseDTO> listExp = expenseService.filterExpense(startDate,endDate,keyword,sort);

            return ResponseEntity.ok(listExp);
        }else {
            return ResponseEntity.badRequest().body("Invalid type");
        }


    }



}
