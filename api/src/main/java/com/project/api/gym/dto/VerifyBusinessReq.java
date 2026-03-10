package com.project.api.gym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyBusinessReq {
    @NotBlank
    @Pattern(regexp="^\\d{10}$", message="사업자등록번호는 숫자 10자리여야 합니다.")
    private String businessRegistrationNumber;

    @NotBlank
    private String legalRepresentativeName;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate businessStartDate;
}

