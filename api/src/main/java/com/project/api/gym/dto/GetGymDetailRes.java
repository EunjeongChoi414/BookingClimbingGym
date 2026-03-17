package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetGymDetailRes {
    private String name;
    private List<BusinessHours> businessHours;
    private String isBusy;
    private String address;
    private List<Pass> passes;
    private String contact;
}
