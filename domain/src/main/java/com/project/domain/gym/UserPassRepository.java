package com.project.domain.gym;

public interface UserPassRepository {
    UserPass getById(String id);

    void add(UserPass userPass);

    boolean isFullyUsed(String userId, String passId);
}
