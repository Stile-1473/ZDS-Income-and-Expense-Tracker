package com.ZerodaySolution.Billing.services;

import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.entity.CategoryEntity;
import com.ZerodaySolution.Billing.entity.ExpenseEntity;
import com.ZerodaySolution.Billing.entity.IncomeEntity;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.CategoryRepository;
import com.ZerodaySolution.Billing.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ProfileService profileService;

    // method to save expense

    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ExpenseEntity expense = toEntity(expenseDTO, profile, category);
        expense = expenseRepository.save(expense);
        return toDTO(expense);
    }

    // rwtrieve all expenses

    public List<ExpenseDTO> getAllExpensesForCurrentMonthForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> expense = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate,
                endDate);
        return expense.stream().map(this::toDTO).toList();

    }

    public void deleteExpense(Long id) {
        ProfileEntity profile = profileService.getCurrentProfile();

        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete");
        }

        expenseRepository.delete(expense);
    }


    //Get latest 5 expense

    public List<ExpenseDTO> getLatestExpenses(){
        ProfileEntity profile = profileService.getCurrentProfile();
       List< ExpenseEntity> latest5Expenses = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());

        return latest5Expenses.stream().map(this::toDTO).toList();

    }

    //total expenses of current user

    public BigDecimal totalExpensesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal expenseTotal =expenseRepository.findTotalExpenseByProfileId(profile.getId());

        return expenseTotal != null ? expenseTotal : BigDecimal.ZERO;

    }

    //Filter expense
    public List<ExpenseDTO> filterExpense(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> foundExpenses = expenseRepository.
                findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);

       return foundExpenses.stream().map(this::toDTO).toList();
    }

    //
    public List<ExpenseDTO> getExpenseOnTheDate(Long profileId,LocalDate date){
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTO).toList();
    }
    // helper methods

    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profile, CategoryEntity category) {

        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .profile(profile)
                .category(category)

                .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity expenseEntity) {

        return ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .icon(expenseEntity.getIcon())
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .categoryId(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId() : null)
                .categoryName(expenseEntity.getCategory() != null ? expenseEntity.getCategory().getName() : "N/A")
                .createdAt(expenseEntity.getCreatedAt())
                .updatedAt(expenseEntity.getUpdatedAt())

                .build();

    }

}