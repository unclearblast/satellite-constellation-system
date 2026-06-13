package com.example.spacecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableScheduling
public class SpaceOperationCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceOperationCenterApplication.class, args);
    }
}

@SpringBootApplication
@EnableCaching
public class SpaceOperationCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpaceOperationCenterApplication.class, args);
    }
}
