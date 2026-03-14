package com.project.infra.gym;

import com.project.domain.gym.UserPass;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserPassRepository implements com.project.domain.gym.UserPassRepository {
    private final Map<String, UserPass> storage = new ConcurrentHashMap<>();

    public void save(UserPass userPass) {
        storage.put(userPass.getId(), userPass);
    }

    @Override
    public UserPass findById(String id) {
        return storage.get(id);
    }
}