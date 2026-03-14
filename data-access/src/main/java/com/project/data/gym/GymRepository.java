package com.project.data.gym;

import com.project.domain.gym.Gym;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GymRepository implements com.project.domain.gym.GymRepository {
    private final Map<String, Gym> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Gym gym) {
        storage.put(gym.getId(), gym);
    }

    @Override
    public Gym findById(String gymId) {
        return storage.get(gymId);
    }

    @Override
    public List<Gym> searchGyms(int page, int size, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return storage.values().stream().toList();
        }
        else {
            return storage.values().stream()
                    .filter(gym -> gym.getName().toLowerCase().contains(keyword.toLowerCase()))
                    .skip((long) (page - 1) * size)
                    .limit(size)
                    .toList();
        }
    }
}