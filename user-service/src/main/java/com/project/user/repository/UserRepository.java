package com.project.user.repository;

import com.project.user.entity.User;

public interface UserRepository {
    User getById(String userId);
    void create(User user);
}
