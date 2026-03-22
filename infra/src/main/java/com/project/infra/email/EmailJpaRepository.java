package com.project.infra.email;

import com.project.domain.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<Email, String> {
}
