package com.project.gym.repository;

import com.project.gym.entity.UserPass;

public interface UserPassRepository {
    UserPass getById(String id);

    void add(UserPass userPass);

    boolean isFullyUsed(String userId, String passId);
}
