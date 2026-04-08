package com.project.gym.repository;

import com.project.gym.entity.UserGym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGymJpaRepository extends JpaRepository<UserGym, String> {
}
