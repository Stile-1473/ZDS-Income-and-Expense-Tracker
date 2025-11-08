package com.ZerodaySolution.Billing.services;


import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.dto.IncomeDTO;
import com.ZerodaySolution.Billing.dto.RecentTransactionDTO;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ProfileService profileService;

    public Map<String,Object> getDashboardData(){
      ProfileEntity profile = profileService.getCurrentProfile();
      Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> incomeTop5 = incomeService.getLatestIncomes();
        List<ExpenseDTO> expenseTop5 = expenseService.getLatestExpenses();

      List<RecentTransactionDTO> recentTransactionDTOList =  concat(incomeTop5.stream().map( income ->
                RecentTransactionDTO.builder()
                        .id(income.getId())
                        .name(income.getName())
                        .icon(income.getIcon())
                        .date(income.getDate())
                        .amount(income.getAmount())
                        .profileId(profile.getId())
                        .createdAt(income.getCreatedAt())
                        .updateAt(income.getUpdatedAt())
                        .type("income")
                        .build()),expenseTop5.stream().map(expense ->

                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .name(expense.getName())
                                .icon(expense.getIcon())
                                .date(expense.getDate())
                                .amount(expense.getAmount())
                                .profileId(profile.getId())
                                .createdAt(expense.getCreatedAt())
                                .updateAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()))
              .sorted((a,b) ->{
                  int cmp =b.getDate().compareTo(a.getDate());

                  if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                  }

                  return cmp;
              }).collect(Collectors.toList());
              returnValue.put("totalBalance",incomeService.totalIncomeForCurrentUser().subtract(expenseService.totalExpensesForCurrentUser()));
              returnValue.put("totalIncome",incomeService.totalIncomeForCurrentUser());
              returnValue.put("totalExpense",expenseService.totalExpensesForCurrentUser());
        returnValue.put("latest5Expense",expenseTop5);
        returnValue.put("latest5Income",incomeTop5);
        returnValue.put("recentTransations",recentTransactionDTOList);

        return returnValue;
    }


}

