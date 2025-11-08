package com.ZerodaySolution.Billing.services;

import com.ZerodaySolution.Billing.dto.ExpenseDTO;
import com.ZerodaySolution.Billing.dto.IncomeDTO;
import com.ZerodaySolution.Billing.entity.CategoryEntity;
import com.ZerodaySolution.Billing.entity.ExpenseEntity;
import com.ZerodaySolution.Billing.entity.IncomeEntity;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.CategoryRepository;
import com.ZerodaySolution.Billing.repository.ExpenseRepository;
import com.ZerodaySolution.Billing.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    //method to save expense


    public  IncomeDTO createIncome(IncomeDTO incomeDTO){
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeEntity incomeEntity = toEntity(incomeDTO,profile,category);
        incomeEntity = incomeRepository.save(incomeEntity);
        return toDTO(incomeEntity);
    }


//monthly incomes
    public List<IncomeDTO> getAllIncomesForCurrentMonthForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List< IncomeEntity > income = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return income.stream().map(this::toDTO).toList();


    }


    //Delete expense
    public void deleteIncome(Long id){
        ProfileEntity profile =profileService.getCurrentProfile();

        IncomeEntity income= incomeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Income not found"));

        if( !income.getProfile().getId().equals(profile.getId())){
                throw  new RuntimeException("Unauthorized to delete");
        }

        incomeRepository.delete(income);
    }

    //Get latest 5 incomes

    public List<IncomeDTO> getLatestIncomes(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List< IncomeEntity> latest5Incomes = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());

        return latest5Incomes.stream().map(this::toDTO).toList();

    }

    //total expenses of current user

    public BigDecimal totalIncomeForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal incomeTotal =incomeRepository.findTotalIncomeByProfileId(profile.getId());

        return incomeTotal != null ? incomeTotal : BigDecimal.ZERO;

    }

    //Filter income
    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> found = incomeRepository.
                findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate,keyword,sort);

        return found.stream().map(this::toDTO).toList();
    }

    //helper methods

    private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profile, CategoryEntity category){

        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .icon(incomeDTO.getIcon())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .profile(profile)
                .category(category)

                .build();
    }

    private IncomeDTO toDTO(IncomeEntity incomeEntity){

        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .icon(incomeEntity.getIcon())
                .amount(incomeEntity.getAmount())
                .date(incomeEntity.getDate())
                .categoryId(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
                .categoryName(incomeEntity.getCategory() != null ? incomeEntity.getCategory().getName() : "N/A")
                .createdAt(incomeEntity.getCreatedAt())
                .updatedAt(incomeEntity.getUpdatedAt())

                .build();

    }

}
