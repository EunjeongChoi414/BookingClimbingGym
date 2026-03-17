package com.project.services.gym.model;

import java.time.LocalDate;

public class UserPassModel {
    private final String passId;
    private final String name;
    private final LocalDate validFrom;
    private final LocalDate validUntil;
    private final int remainingUses;

    public UserPassModel(
            String passId, String name, LocalDate validFrom, LocalDate validUntil, int remainingUses) {
        this.passId = passId;
        this.name = name;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.remainingUses = remainingUses;
    }

    public String getPassId() {
        return passId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public int getRemainingUses() {
        return remainingUses;
    }
}
