package com.project.domain.gym;

import com.project.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserPass {
    private final String id;
    private final Pass pass;
    private final User user;
    private final LocalDate validFrom;
    private final LocalDate validUntil;
    private int remainingUses;
    private LocalDateTime lastUsedAt;

    public UserPass(
            Pass pass, User user, LocalDate validFrom, LocalDate validUntil, int remainingUses) {
        this.id = UUID.randomUUID().toString();
        this.pass = pass;
        this.user = user;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.remainingUses = remainingUses;
    }

    public String getId() { return id; }
    public Pass getPass() { return pass; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
    public int getRemainingUses() { return remainingUses; }

    public boolean isValid() {
        return remainingUses <= pass.getMaxUses() && remainingUses > 0;
    }

    public void usePass() {
        if (isValid()) {
            remainingUses --;
            lastUsedAt = LocalDateTime.now();
        }
        else {
            throw new RuntimeException("Invalid pass");
        }
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }
}
