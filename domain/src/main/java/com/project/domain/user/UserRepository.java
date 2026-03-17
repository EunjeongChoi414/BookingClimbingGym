package com.project.domain.user;

import java.util.Optional;

public interface UserRepository {
    User findById(String userId);
    void create(User user);
}
