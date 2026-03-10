package com.project.api.gym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHours {
    @NotNull
    private DayOfWeek Day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime StartTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime EndTime;
}
