package com.project.data.user;

import com.project.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository implements com.project.domain.user.UserRepository {
    @Override
    public Optional<User> findById(String userId) {
        return Optional.empty();
    }

    @Override
    public void create(User user) {

    }
}
