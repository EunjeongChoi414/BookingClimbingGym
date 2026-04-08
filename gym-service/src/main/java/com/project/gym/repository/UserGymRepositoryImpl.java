package com.project.gym.repository;

import com.project.gym.entity.UserGym;
import com.project.gym.repository.UserGymRepository;
import org.springframework.stereotype.Repository;


@Repository
public class UserGymRepositoryImpl implements UserGymRepository {
    private final UserGymJpaRepository jpaRepository;

    public UserGymRepositoryImpl(UserGymJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserGym findByIds(String gymId, String userId) {
        return jpaRepository.findById(gymId).orElse(null);
    }
}
