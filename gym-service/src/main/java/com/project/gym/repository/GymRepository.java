package com.project.gym.repository;

import com.project.gym.entity.Gym;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GymRepository extends JpaRepository<Gym, String> {

    List<Gym> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    default Gym getById(String gymId) {
        return findById(gymId).orElseThrow();
    }

    default List<Gym> searchGyms(int page, int size, String keyword) {
        return findByNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
    }
}