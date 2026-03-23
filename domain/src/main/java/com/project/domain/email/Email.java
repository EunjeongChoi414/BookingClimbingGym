package com.project.domain.email;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Email {
    @Id
    private String id;

    private String code;

    protected Email() {
    }

    public Email(String emailAddress) {
        this.id = emailAddress;
        this.code = UUID.randomUUID().toString().substring(0, 6);
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }
}
