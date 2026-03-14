package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class BookingDetail {
    public String id;
    public String userId;
    public LocalDateTime dateTime;
    public String passId;
}
