package com.project.domain.email;

import java.util.Optional;

public interface EmailRepository {
    void add(String email, String code);

    String findCodeByEmail(String email);

    void delete(String email);
}
