package com.project.domain.gym;

import java.util.List;
import java.util.Optional;

public interface GymRepository {
    void save(Gym gym);
    Gym findById(String gymId);
    List<Gym> searchGyms(int page, int size, String keyword);
}