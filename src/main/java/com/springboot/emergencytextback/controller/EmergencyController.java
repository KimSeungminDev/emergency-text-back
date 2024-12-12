package com.springboot.emergencytextback.controller;

import com.springboot.emergencytextback.entity.Emergency;
import com.springboot.emergencytextback.service.EmergencyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "${spring.app.url}")  // .env에서 값을 읽어서 CORS 허용
@RequestMapping("/text")
public class EmergencyController {

    private final EmergencyService emergencyService;

    // .env에서 값 읽어오기
    @Value("${spring.app.url}")
    private String springAppUrl;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @GetMapping
    public List<Emergency> getEmergencies(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String message,
            HttpSession session) {
        // sessionName 생성 및 세션에 저장
        if ((region != null && !region.isBlank()) || (message != null && !message.isBlank())) {
            String searchRegion = (region == null || region.isBlank()) ? "null" : region.trim();
            String searchMessage = (message == null || message.isBlank()) ? "null" : message.trim();
            String searchTerm = searchRegion + "-" + searchMessage;

            String searchTime = emergencyService.getCurrentTime();
            String sessionName = searchTerm + "-" + searchTime;

            session.setAttribute("sessionName", sessionName);
            System.out.println(springAppUrl);
        }

        return emergencyService.fetchMessages(region, message); // 서비스 호출
    }

}
