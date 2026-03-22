package com.project.infra.email;

import com.project.domain.email.Email;
import com.project.domain.email.EmailRepository;
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
