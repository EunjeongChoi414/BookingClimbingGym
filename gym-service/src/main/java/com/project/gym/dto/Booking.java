package com.project.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Booking {
    public String id;
    public String userId;
    public LocalDateTime dateTime;
    public String passId;
}
