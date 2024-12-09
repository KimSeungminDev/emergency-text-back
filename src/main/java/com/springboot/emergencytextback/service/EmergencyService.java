package com.springboot.emergencytextback.service;

import com.springboot.emergencytextback.dto.ApiResponse;
import com.springboot.emergencytextback.entity.Emergency;
import com.springboot.emergencytextback.repository.EmergencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EmergencyService {

    private static final Logger logger = LoggerFactory.getLogger(EmergencyService.class);

    private final RestTemplate restTemplate;
    private final EmergencyRepository emergencyRepository;

    @Value("${external.api.url}")
    private String apiUrl;

    @Value("${external.api.key}")
    private String serviceKey;

    public EmergencyService(RestTemplate restTemplate, EmergencyRepository emergencyRepository) {
        this.restTemplate = restTemplate;
        this.emergencyRepository = emergencyRepository;
    }

    /**
     * 메시지를 대상 지역(region)과 문자 내용(message)을 기준으로 가져오는 메서드
     *
     * @param region  대상 지역
     * @param message 문자 내용
     * @return 필터링된 Emergency 리스트
     */
    public List<Emergency> fetchMessages(String region, String message) {
        List<Emergency> allMessages = fetchAllMessagesFromAPI();

        // 검색어 결합
        String searchRegion = (region == null || region.isBlank()) ? "null" : region.trim();
        String searchMessage = (message == null || message.isBlank()) ? "null" : message.trim();
        String searchTerm = searchRegion + "-" + searchMessage;

        // 검색어가 없으면 전체 메시지 정렬 후 반환
        if ("null-null".equals(searchTerm)) {
            return allMessages.stream()
                    .sorted(Comparator.comparing(Emergency::getSn)) // SN 기준 정렬
                    .toList();
        }

        // 검색어로 필터링된 메시지
        List<Emergency> filteredMessages = allMessages.stream()
                .filter(msg -> matchesSearchTerm(msg, searchRegion, searchMessage)) // 필터링 조건
                .sorted(Comparator.comparing(Emergency::getSn)) // SN 기준 정렬
                .toList();

        // 필터링된 메시지 저장
        if (!filteredMessages.isEmpty()) {
            saveMessagesWithSearchTerm(filteredMessages, region, message);
        }

        return filteredMessages;
    }

    private boolean matchesSearchTerm(Emergency emergency, String region, String message) {
        return (region.equals("null") || (emergency.getRcptnRgnNm() != null && emergency.getRcptnRgnNm().contains(region)))
                && (message.equals("null") || (emergency.getMsgCn() != null && emergency.getMsgCn().contains(message)));
    }


    /**
     * 대상 지역(region)과 문자 내용(message)을 기준으로 메시지가 일치하는지 확인
     *
     * @param emergency Emergency 객체
     * @param region    대상 지역
     * @param message   문자 내용
     * @return 일치 여부
     */
    private boolean matchesRegionAndMessage(Emergency emergency, String region, String message) {
        boolean matchesRegion = region == null || region.isBlank() ||
                (emergency.getRcptnRgnNm() != null && emergency.getRcptnRgnNm().toLowerCase().contains(region.toLowerCase()));

        boolean matchesMessage = message == null || message.isBlank() ||
                (emergency.getMsgCn() != null && emergency.getMsgCn().toLowerCase().contains(message.toLowerCase()));

        return matchesRegion && matchesMessage;
    }

    /**
     * 외부 API에서 모든 메시지를 가져오는 메서드
     *
     * @return Emergency 리스트
     */
    private List<Emergency> fetchAllMessagesFromAPI() {
        List<Emergency> allMessages = new ArrayList<>();
        String sevenDaysAgo = getSevenDaysAgoDate();
        int totalPages = 1;

        try {
            ApiResponse initialResponse = fetchPage(sevenDaysAgo, 1);
            if (initialResponse != null && initialResponse.getBody() != null) {
                allMessages.addAll(initialResponse.getBody());
                totalPages = (int) Math.ceil((double) initialResponse.getHeader().getTotalCount() / 1000);
            }

            for (int pageNo = 2; pageNo <= totalPages; pageNo++) {
                ApiResponse nextPageResponse = fetchPage(sevenDaysAgo, pageNo);
                if (nextPageResponse != null && nextPageResponse.getBody() != null) {
                    allMessages.addAll(nextPageResponse.getBody());
                }
            }

            allMessages.forEach(this::extractMsgSend);

        } catch (Exception e) {
            logger.error("Error fetching data from external API", e);
        }

        return allMessages;
    }

    /**
     * 특정 페이지의 데이터를 API에서 가져오는 메서드
     *
     * @param sevenDaysAgo 7일 전 날짜
     * @param pageNo       페이지 번호
     * @return ApiResponse
     */
    private ApiResponse fetchPage(String sevenDaysAgo, int pageNo) {
        String url = apiUrl + "?serviceKey=" + serviceKey +
                "&crtDt=" + sevenDaysAgo +
                "&numOfRows=1000&pageNo=" + pageNo;

        ResponseEntity<ApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ApiResponse.class
        );
        return response.getBody();
    }

    /**
     * MSG_CN에서 MSG_SEND를 추출하는 메서드
     *
     * @param emergency Emergency 객체
     */
    private void extractMsgSend(Emergency emergency) {
        if (emergency.getMsgCn() != null) {
            String msgCn = emergency.getMsgCn();
            int startIndex = msgCn.indexOf('[');
            int endIndex = msgCn.indexOf(']');
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                emergency.setMsgSend(msgCn.substring(startIndex + 1, endIndex));
            } else {
                emergency.setMsgSend("Unknown");
            }
        } else {
            emergency.setMsgSend("Unknown");
        }
    }

    public void saveMessagesWithSearchTerm(List<Emergency> messages, String region, String message) {
        // 기본 값 설정
        String searchRegion = (region == null || region.isBlank()) ? "null" : region.trim();
        String searchMessage = (message == null || message.isBlank()) ? "null" : message.trim();
        String searchTerm = searchRegion + "-" + searchMessage;

        // 저장하지 않을 조건: region과 message가 모두 null인 경우
        if ("null-null".equals(searchTerm)) {
            return; // 저장하지 않음
        }

        // 검색 시간을 추가
        String searchTime = getCurrentTime();
        String sessionName = searchTerm + "-" + searchTime;

        // 메시지 저장 준비
        for (Emergency emergency : messages) {
            emergency.setSearchTerm(searchTerm);
            emergency.setSearchTime(searchTime);
            emergency.setSessionName(sessionName);
        }

        // SN 기준으로 정렬 후 저장
        List<Emergency> sortedMessages = messages.stream()
                .sorted(Comparator.comparing(Emergency::getSn)) // SN 기준 정렬
                .toList();

        emergencyRepository.saveAll(sortedMessages); // 저장
    }


    public String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private String getSevenDaysAgoDate() {
        return LocalDateTime.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
