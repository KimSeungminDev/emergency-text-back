package com.springboot.emergencytextback;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EmergencyTextBackApplication {
    public static void main(String[] args) {
        // Load environment variables from .env
        Dotenv dotenv = Dotenv.configure().load();

        // Set system properties for Spring Boot to read
        System.setProperty("SPRING_APP_URL", dotenv.get("SPRING_APP_URL"));
        System.setProperty("RDS_ENDPOINT", dotenv.get("RDS_ENDPOINT"));
        System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("API_URL", dotenv.get("API_URL"));
        System.setProperty("API_KEY", dotenv.get("API_KEY"));

        SpringApplication.run(EmergencyTextBackApplication.class, args);
    }

    // Register RestTemplate as a Spring Bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
