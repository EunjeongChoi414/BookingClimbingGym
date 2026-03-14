package com.project.domain.gym;

public interface UserGymRepository {
    UserGym findByIds(String gymId, String userId);
}
