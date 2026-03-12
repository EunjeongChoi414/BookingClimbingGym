package com.project.data.email;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmailRepository implements com.project.domain.email.EmailRepository {
    private final Map<String, String> storage = new ConcurrentHashMap<>();

    @Override
    public void save(String email, String code) {
        storage.put(email, code);
    }

    @Override
    public Optional<String> findByEmail(String email) {
        return Optional.ofNullable(storage.get(email));
    }

    @Override
    public void delete(String email) {
        storage.remove(email);
    }
}