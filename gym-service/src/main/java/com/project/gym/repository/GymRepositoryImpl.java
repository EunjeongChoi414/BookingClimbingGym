package com.project.gym.repository;

import com.project.gym.entity.Gym;
import com.project.gym.repository.GymRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GymRepositoryImpl implements GymRepository {

    private final GymJpaRepository jpaRepository;

    public GymRepositoryImpl(GymJpaRepository gymJpaRepository) {
        this.jpaRepository = gymJpaRepository;
    }

    @Override
    public void add(Gym gym) {
        jpaRepository.save(gym);
    }

    @Override
    public Gym getById(String gymId) {
        return jpaRepository.findById(gymId).orElse(null);
    }

    @Override
    public List<Gym> searchGyms(int page, int size, String keyword) {
        return List.of();
    }
}
