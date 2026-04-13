package com.project.gym.entity;

import com.project.common.exception.DomainException;
import com.project.common.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserPass {
    @Getter
    @Id
    private String id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "pass_id")
    private Pass pass;

    @Getter
    private String userId;

    @Getter
    private LocalDate validFrom;
    @Getter
    private LocalDate validUntil;
    private Integer remainingUses;
    private LocalDateTime lastUsedAt;

    protected UserPass() {
    }

    public UserPass(Pass pass, String userId, LocalDate validFrom) {
        this.id = UUID.randomUUID().toString();
        this.pass = pass;
        this.userId = userId;
        this.validFrom = validFrom;
        this.validUntil = validFrom.plusDays(pass.getValidDays());
        this.remainingUses = pass.getMaxUses();
    }

    public int getRemainingUses() {
        return remainingUses;
    }

    public void validate(Clock clock) {
        if (remainingUses <= 0) {
            throw new DomainException(ErrorCode.NO_REMAINING_USES);
        }
        LocalDate now = LocalDate.now(clock);
        if (now.isAfter(validUntil) || now.isBefore(validFrom)) {
            throw new DomainException(ErrorCode.INVALID_PASS);
        }
    }

    public void usePass(Clock clock) {
        validate(clock);
        remainingUses--;
        lastUsedAt = LocalDateTime.now();
    }

}