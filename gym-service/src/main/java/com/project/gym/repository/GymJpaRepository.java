package com.project.gym.repository;

import com.project.gym.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymJpaRepository extends JpaRepository<Gym, String> {
}
