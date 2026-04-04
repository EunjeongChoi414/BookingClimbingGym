package com.project.gym.repository;

import com.project.gym.entity.UserGym;

public interface UserGymRepository {
    UserGym findByIds(String gymId, String userId);
}
