package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetGymMyDetailRes {
    private List<UserPass> passes;
    private List<UserBooking> bookings;
}
