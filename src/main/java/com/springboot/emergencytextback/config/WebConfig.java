package com.springboot.emergencytextback.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String springAppUrl;

    public WebConfig() {
        // .env 파일에서 값 읽어오기
        Dotenv dotenv = Dotenv.configure().load();
        this.springAppUrl = dotenv.get("SPRING_APP_URL");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 엔드포인트에 대해 CORS 허용
        registry.addMapping("/**")
                .allowedOrigins(springAppUrl + ":3000") // 환경 변수로 설정된 값
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 자격증명 포함 요청 허용
    }
}
