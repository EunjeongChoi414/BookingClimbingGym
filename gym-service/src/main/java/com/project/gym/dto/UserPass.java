package com.project.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UserPass {
    private String id;

    private String name;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private int remainingUses;
}
