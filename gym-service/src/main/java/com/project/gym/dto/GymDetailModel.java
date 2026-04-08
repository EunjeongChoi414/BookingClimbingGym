package com.project.gym.dto;

import java.util.List;

public record GymDetailModel(String name, List<BusinessHoursModel> businessHours,
                             String isBusy, String address, List<PassModel> passes, String contact) {
}
