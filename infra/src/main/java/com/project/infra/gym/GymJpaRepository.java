package com.project.infra.gym;

import com.project.domain.gym.Gym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymJpaRepository extends JpaRepository<Gym, String> {
}
