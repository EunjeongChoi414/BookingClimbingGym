package com.project.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@SpringBootApplication(scanBasePackages = {"com.project.user", "com.project.common"})
@EntityScan("com.project.user")
@EnableJpaRepositories("com.project.user")
public class UserTestApplication {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}