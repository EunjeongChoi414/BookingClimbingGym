package com.project.gym;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Clock;

@SpringBootApplication(scanBasePackages = {"com.project.gym", "com.project.user", "com.project.common"})
@EntityScan("com.project")
@EnableJpaRepositories("com.project")
public class GymTestApplication {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}