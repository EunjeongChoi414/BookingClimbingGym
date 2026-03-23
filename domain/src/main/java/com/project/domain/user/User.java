package com.project.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private LocalDateTime lastLoginAt;
    private boolean isManager;

    protected User() {
    }

    public User(String email, String password, Clock clock) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.lastLoginAt = LocalDateTime.now(clock);
        this.isManager = false;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }
}
