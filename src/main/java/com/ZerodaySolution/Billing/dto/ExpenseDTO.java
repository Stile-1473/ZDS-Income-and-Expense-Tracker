package com.ZerodaySolution.Billing.dto;

import com.ZerodaySolution.Billing.entity.CategoryEntity;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExpenseDTO {


    private Long id;

    private String name;

    private String icon;
    private String categoryName;
    private Long categoryId;
    private LocalDate date;

    private BigDecimal amount;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

   // private Long profileId;



}
