package com.project.gym.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Pass {
    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private Integer maxUses;
    private int validDays;

    protected Pass() {
    }

    public Pass(String name, BigDecimal price, Integer maxUses, int validDays) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.maxUses = maxUses;
        this.validDays = validDays;
    }

    public String getId() {
        return id;
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