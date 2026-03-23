package com.project.infra.user;

import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User getById(String userId) {
        return jpaRepository.findById(userId).orElse(null);
    }

    @Override
    public void create(User user) {
        jpaRepository.save(user);
    }
}
