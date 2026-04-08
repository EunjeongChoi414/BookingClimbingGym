package com.project.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class UserBooking {
    private String id;

    private LocalDateTime startDateTime;
}
