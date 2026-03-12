package com.project.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);
    void create(User user);
}
