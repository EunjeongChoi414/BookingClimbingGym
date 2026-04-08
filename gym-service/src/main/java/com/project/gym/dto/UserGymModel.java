package com.project.gym.dto;

import java.util.List;

public record UserGymModel(List<UserPassModel> pass, List<BookingModel> bookings) {
}
