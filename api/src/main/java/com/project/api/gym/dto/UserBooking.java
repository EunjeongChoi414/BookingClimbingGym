package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class UserBooking {
    private String id;

    private LocalDate date;

    private LocalTime startTime;
}
