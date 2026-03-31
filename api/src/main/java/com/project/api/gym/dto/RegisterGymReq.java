package com.project.api.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterGymReq {
    @NotBlank
    private String gymName;

    @NotBlank
    private String gymAddress;

    @NotEmpty(message = "적어도 하나 이상의 패스를 제공해야합니다.")
    private List<Pass> passes;

    @NotBlank
    private String contact;

    @Size(min = 7, max = 7, message = "영업시간은 정확히 7개여야 합니다.")
    private List<BusinessHours> businessHours;

    @Positive
    private int maxCapacity;

    private int cancellationNoticeDays;
}

