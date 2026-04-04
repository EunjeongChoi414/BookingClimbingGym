package com.project.gym.dto;

import java.util.List;

public record GymPreviewModel(
        String name,
        List<BusinessHoursModel> businessHours,
        String currentCrowdLevel,
        String address
) {
}
