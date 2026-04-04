package com.project.user.repository;

import com.project.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User getById(String userId) {
        return jpaRepository.findById(userId).orElseThrow();
    }

    @Override
    public void create(User user) {
        jpaRepository.save(user);
    }
}
