package com.project.infra.gym;

import com.project.domain.gym.UserGym;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserGymRepository implements com.project.domain.gym.UserGymRepository {
    private final Map<String, UserGym> storage = new ConcurrentHashMap<>();

    @Override
    public UserGym findByIds(String gymId, String userId) {
        for  (UserGym userGym : storage.values()) {
            if (userGym.getGym().getId().equals(gymId) && userGym.getUser().getId().equals(userId)) {
                return userGym;
            }
        }
        return null;
    }
}
