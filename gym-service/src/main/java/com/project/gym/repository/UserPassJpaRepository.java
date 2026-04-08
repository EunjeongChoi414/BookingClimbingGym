package com.project.gym.repository;

import com.project.gym.entity.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPassJpaRepository extends JpaRepository<UserPass, String> {
    boolean existsByUserIdAndPass_IdAndRemainingUsesGreaterThan(String userId, String passId, int remainingUses);
}
