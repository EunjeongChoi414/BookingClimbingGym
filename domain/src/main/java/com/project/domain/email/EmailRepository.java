package com.project.domain.email;

import java.util.Optional;

public interface EmailRepository {
    void save(String email, String code);

    Optional<String> findByEmail(String email);

    void delete(String email);
}
