package com.project.infra.gym;

import com.project.domain.gym.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPassJpaRepository extends JpaRepository<UserPass, String> {
    boolean existsByUserIdAndPass_IdAndRemainingUsesGreaterThan(String userId, String passId, int remainingUses);
}
