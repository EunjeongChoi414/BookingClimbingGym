package com.project.services.gym.model;

import java.util.List;

public record GymPreviewModel(
        String name,
        List<BusinessHoursModel> businessHours,
        String currentCrowdLevel,
        String address
) {
}
