package com.project.domain.gym;

import com.project.domain.exception.InvalidPassException;
import com.project.domain.exception.NoRemainingUsesException;
import com.project.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserPass {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "pass_id")
    private Pass pass;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate validFrom;
    private LocalDate validUntil;
    private int remainingUses;
    private LocalDateTime lastUsedAt;

    protected UserPass() {
    }

    public UserPass(Pass pass, User user, LocalDate validFrom) {
        this.id = UUID.randomUUID().toString();
        this.pass = pass;
        this.user = user;
        this.validFrom = validFrom;
        this.validUntil = validFrom.plusDays(pass.getValidDays());
        this.remainingUses = pass.getMaxUses();
    }

    public String getId() {
        return id;
    }

    public Pass getPass() {
        return pass;
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

    public void validate(Clock clock) {
        if (remainingUses <= 0) {
            throw new NoRemainingUsesException();
        }
        LocalDate now = LocalDate.now(clock);
        if (now.isAfter(validUntil) || now.isBefore(validFrom)) {
            throw new InvalidPassException();
        }
    }

    public void usePass(Clock clock) {
        validate(clock);
        remainingUses--;
        lastUsedAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }
}