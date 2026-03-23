package com.project.domain.user;

public interface UserRepository {
    User getById(String userId);
    void create(User user);
}
