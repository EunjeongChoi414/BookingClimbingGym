package com.project.domain.email;

public interface EmailRepository {
    void add(Email email);

    Email findEmailById(String email);

    void delete(Email email);
}
