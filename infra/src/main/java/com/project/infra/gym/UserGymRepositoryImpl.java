package com.project.infra.gym;

import com.project.domain.gym.UserGym;
import com.project.domain.gym.UserGymRepository;
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
