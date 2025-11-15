package com.backend.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {

    /**
     * Entry point of the Spring Boot application.
     *
     * SpringApplication.run():
     * - Starts the embedded Tomcat server
     * - Performs component scanning
     * - Loads the Spring application context
     * - Initializes all @Controller, @Service, @Repository, etc.
     *
     * This class must be located at a package root so that all
     * sub-packages (Controller, Service, Model...) are automatically scanned.
     */

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
