package com.project.domain.gym;

public interface UserPassRepository {
    UserPass getById(String id);

    void add(UserPass userPass);

    void update(UserPass userPass);
}
