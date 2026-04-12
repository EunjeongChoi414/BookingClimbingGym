package com.project.gym.repository;

import com.project.gym.entity.UserPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPassRepository extends JpaRepository<UserPass, String> {

    boolean existsByUserIdAndPass_IdAndRemainingUsesGreaterThan(String userId, String passId, int remainingUses);

    default UserPass getById(String id) {
        return findById(id).orElseThrow();
    }

    default boolean isFullyUsed(String userId, String passId) {
        return !existsByUserIdAndPass_IdAndRemainingUsesGreaterThan(userId, passId, 0);
    }
}
