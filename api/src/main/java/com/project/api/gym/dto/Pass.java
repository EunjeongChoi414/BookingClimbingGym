package com.project.api.gym.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Pass {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Positive
    private Integer maxUses;

    @Positive(message = "유효기간은 1일 이상이어야 합니다.")
    private int validDays;
}
