package com.project.email.repository;

import com.project.email.entity.Email;

public interface EmailRepository {
    void add(Email email);

    Email findEmailById(String email);

    void delete(Email email);
}
