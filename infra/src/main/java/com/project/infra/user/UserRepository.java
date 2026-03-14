package com.project.infra.user;

import com.project.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository implements com.project.domain.user.UserRepository {
    private final Map<String, User> storage = new HashMap<>();

    @Override
    public User findById(String userId) {
        return storage.get(userId);
    }

    @Override
    public void create(User user) {
        storage.put(user.getId(), user);
    }
}
