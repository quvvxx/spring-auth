package com.cy.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringAuthPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthPracticeApplication.class, args);
    }

}
