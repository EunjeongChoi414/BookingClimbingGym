package com.project.domain.gym;

import java.util.List;

public interface GymRepository {
    void add(Gym gym);

    Gym getById(String gymId);

    List<Gym> searchGyms(int page, int size, String keyword);
}