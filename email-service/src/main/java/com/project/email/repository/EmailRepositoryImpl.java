package com.project.email.repository;

import com.project.email.entity.Email;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRepositoryImpl implements EmailRepository {

    private final EmailJpaRepository jpaRepository;

    public EmailRepositoryImpl(EmailJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void add(Email email) {
        jpaRepository.save(email);
    }

    @Override
    public Email findEmailById(String email) {
        return jpaRepository.findById(email).orElse(null);
    }

    @Override
    public void delete(Email email) {
        jpaRepository.delete(email);
    }
}
