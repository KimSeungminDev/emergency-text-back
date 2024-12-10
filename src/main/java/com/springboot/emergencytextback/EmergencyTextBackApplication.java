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
        System.setProperty("RDS_ENDPOINT", dotenv.get("RDS_ENDPOINT"));
        System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
        System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("API_KEY", dotenv.get("API_KEY"));
        System.setProperty("spring.datasource.url",
                String.format("jdbc:mariadb://%s:%s/%s",
                        dotenv.get("RDS_ENDPOINT"), // RDS 엔드포인트
                        dotenv.get("DB_PORT"),      // 포트 번호
                        dotenv.get("DB_NAME")       // 데이터베이스 이름
                )
        );
        System.setProperty("external.api.url", dotenv.get("API_URL"));
        System.setProperty("spring.app.url", dotenv.get("SPRING_APP_URL"));

        SpringApplication.run(EmergencyTextBackApplication.class, args);
    }

    // Register RestTemplate as a Spring Bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
