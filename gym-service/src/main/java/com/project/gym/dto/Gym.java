package com.project.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    private String name;
    private List<BusinessHours>  businessHours;
    private String currentCrowdLevel;
    private String address;
}
