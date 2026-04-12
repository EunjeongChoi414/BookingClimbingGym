package com.project.gym.repository;

import com.project.gym.entity.UserGym;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGymRepository extends JpaRepository<UserGym, String> {

    Optional<UserGym> findByGym_IdAndUser_Id(String gymId, String userId);

    default UserGym findByIds(String gymId, String userId) {
        return findByGym_IdAndUser_Id(gymId, userId).orElse(null);
    }
}
