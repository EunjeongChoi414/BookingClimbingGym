package com.project.services.gym.model;

import java.util.List;

public record GymDetailModel(String name, List<BusinessHoursModel> businessHours,
                             String isBusy, String address, List<PassModel> passes, String contact) {
}
