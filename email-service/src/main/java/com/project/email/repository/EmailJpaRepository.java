package com.project.email.repository;

import com.project.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<Email, String> {
}
