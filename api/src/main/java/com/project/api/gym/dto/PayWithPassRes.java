package com.project.api.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PayWithPassRes {
    private String bookingId;
    private int remainingUses;
    private String qrToken;
}
