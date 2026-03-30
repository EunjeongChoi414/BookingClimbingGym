package com.project.services.gym.model;

import java.math.BigDecimal;

public class PassModel {
    private final String name;
    private final BigDecimal price;
    private final Integer maxUses;
    private final int validDays;

    public PassModel(String name, BigDecimal price, Integer maxUses, int validDays) {
        this.name = name;
        this.price = price;
        this.maxUses = maxUses;
        this.validDays = validDays;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public int getValidDays() {
        return validDays;
    }
}