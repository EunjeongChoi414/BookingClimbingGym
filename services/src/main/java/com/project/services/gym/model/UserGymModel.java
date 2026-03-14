package com.project.services.gym.model;

import java.util.List;

public record UserGymModel(List<UserPassModel> pass, List<UserBookingModel> bookings) {
}
