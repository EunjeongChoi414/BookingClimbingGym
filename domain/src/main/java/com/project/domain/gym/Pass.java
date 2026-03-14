package com.project.domain.gym;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Pass {
    @Id
    private final String id;
    private final String name;
    private final BigDecimal price;
    private final int maxUses;
    private final int validDays;

    public Pass(String name, BigDecimal price, int maxUses, int validDays) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.maxUses = maxUses;
        this.validDays = validDays;
    }

    public String getId() {return id;}
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getMaxUses() { return maxUses; }
    public int getValidDays() { return validDays; }
}