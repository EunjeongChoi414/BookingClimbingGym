package com.project.infra.gym;

import com.project.domain.gym.UserPass;
import com.project.domain.gym.UserPassRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserPassRepositoryImpl implements UserPassRepository {

    private final UserPassJpaRepository jpaRepository;

    public UserPassRepositoryImpl(UserPassJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public UserPass getById(String id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public void add(UserPass userPass) {
        jpaRepository.save(userPass);
    }

    @Override
    public void update(UserPass userPass) {
    }
}
