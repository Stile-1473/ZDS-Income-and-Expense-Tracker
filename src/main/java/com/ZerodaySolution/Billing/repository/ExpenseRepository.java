package com.ZerodaySolution.Billing.repository;

import com.ZerodaySolution.Billing.entity.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public  interface  ExpenseRepository extends JpaRepository < ExpenseEntity,Long> {
    //finds expenses for the user by id
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);
    //returns top 5 expenses
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);
    //returns the total of the expenses
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.profile.id = :profileId ")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort

    );

    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

List<ExpenseEntity>findByProfileIdAndDate(Long profileId,LocalDate date);
}
