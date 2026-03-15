package com.project.domain.gym;

public interface UserPassRepository {
    UserPass findById(String id);

    void add(UserPass userPass);

    void update(UserPass userPass);
}
