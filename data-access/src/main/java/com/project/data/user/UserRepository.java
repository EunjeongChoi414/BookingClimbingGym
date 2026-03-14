package com.project.data.user;

import com.project.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository implements com.project.domain.user.UserRepository {
    @Override
    public User findById(String userId) {
        return new User("", "");
    }

    @Override
    public void create(User user) {

    }
}
