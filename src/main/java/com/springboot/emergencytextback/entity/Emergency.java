package com.springboot.emergencytextback.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Emergency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // id가 자동 증가하는 기본 키

    @JsonProperty("MSG_CN")
    @Column(columnDefinition = "TEXT")
    private String msgCn;  // 메시지 내용

    @JsonProperty("RCPTN_RGN_NM")
    @Column(columnDefinition = "TEXT")
    private String rcptnRgnNm;  // 수신 지역명

    @JsonProperty("REG_YMD")
    private String regYmd;  // 등록 일자

    @JsonProperty("EMRG_STEP_NM")
    private String emrgStepNm;  // 긴급 단계명

    @JsonProperty("SN")
    private Long sn;  // 중복 가능

    @JsonProperty("DST_SE_NM")
    private String dstSeNm;  // 재해 구분명

    @JsonProperty("MDFCN_YMD")
    private String mdfcnYmd;  // 수정 일자

    @JsonProperty("CRT_DT")
    private String crtDt;  // 생성 일자

    private String msgSend;  // 발신자

    private String searchTerm;  // 검색어
    private String searchTime;  // 검색 시간

    @Column(nullable = false) // NOT NULL 설정
    private String sessionName;

}
