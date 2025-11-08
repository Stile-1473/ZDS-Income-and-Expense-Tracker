package com.ZerodaySolution.Billing.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {

    private long id;

    @NotNull
    @JsonProperty("fullName")
    @JsonAlias("fullname")
    private String fullName;

    private String email;

    private String password;

    private String profileImageUrl;

    private LocalDate createdAt;

    private LocalDate updatedAt;

}
