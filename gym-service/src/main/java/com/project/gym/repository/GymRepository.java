package com.project.gym.repository;

import com.project.gym.entity.Gym;
import java.util.List;

public interface GymRepository {
    void add(Gym gym);

    Gym getById(String gymId);

    List<Gym> searchGyms(int page, int size, String keyword);
}