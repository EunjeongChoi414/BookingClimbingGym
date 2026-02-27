package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    private String name;
    private String photoUri;
    private boolean isOpen;
    private String currentCrowdLevel;
    private String address;
}
