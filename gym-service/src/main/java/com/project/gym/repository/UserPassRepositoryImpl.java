package com.project.gym.repository;

import com.project.gym.entity.UserPass;
import com.project.gym.repository.UserPassRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserPassRepositoryImpl implements UserPassRepository {

    private final UserPassJpaRepository jpaRepository;

    public UserPassRepositoryImpl(UserPassJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserPass getById(String id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public void add(UserPass userPass) {
        jpaRepository.save(userPass);
    }

    @Override
    public boolean isFullyUsed(String userId, String passId) {
        return !jpaRepository.existsByUserIdAndPass_IdAndRemainingUsesGreaterThan(userId, passId, 0);
    }
}
