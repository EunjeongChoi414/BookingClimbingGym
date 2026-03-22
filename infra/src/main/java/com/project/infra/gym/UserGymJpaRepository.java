package com.project.infra.gym;

import com.project.domain.gym.UserGym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGymJpaRepository extends JpaRepository<UserGym, String> {
}
