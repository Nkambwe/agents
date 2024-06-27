package com.pbu.wendi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.pbu.wendi.repositories")
@EntityScan(basePackages = "com.pbu.wendi.model")

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
