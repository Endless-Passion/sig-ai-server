package com.endlesspassion.sigai.global.common.enums;

import lombok.Getter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 서울시 25개 자치구별 대표 상권 Enum (해커톤 데모용)
 * @Getter 로 getter 메소드 자동 생성
 * 이후에 모든 상권 DB에 넣고 엘라스틱 서치로 구현하기!
 */
@Getter
public enum SeoulGuCommercialArea {

    // 강북
    A_3110019("3110019", "창덕궁"),            // 종로구
    A_3110039("3110039", "회현역 1번"),        // 중구 (남대문시장)
    A_3110086("3110086", "이태원역 북측"),      // 용산구
    A_3110120("3110120", "서울숲카페거리"),     // 성동구
    A_3110141("3110141", "건대입구역 1번"),    // 광진구
    A_3110189("3110189", "제기동역 1번"),      // 동대문구 (경동시장)
    A_3110265("3110265", "망우역 1번"),        // 중랑구
    A_3110303("3110303", "성신여대입구역 1번"), // 성북구
    A_3110367("3110367", "강북구청"),           // 강북구
    A_3110406("3110406", "도봉산역 1번"),      // 도봉구
    A_3110422("3110422", "노원역 9번"),        // 노원구
    A_3110464("3110464", "연신내역 5번"),      // 은평구

    // 강서
    A_3110505("3110505", "신촌역 1번"),        // 서대문구
    A_3110543("3110543", "홍대입구역 8번"),    // 마포구
    A_3110582("3110582", "목동역 1번"),        // 양천구
    A_3110629("3110629", "마곡역 3번"),        // 강서구
    A_3110668("3110668", "구로디지털단지역 1번"), // 구로구
    A_3110700("3110700", "가산디지털단지역 7번"), // 금천구
    A_3110729("3110729", "여의도역 5번"),      // 영등포구
    A_3110762("3110762", "노량진역 1번"),      // 동작구
    A_3110800("3110800", "서울대입구역 2번"),  // 관악구

    // 강남
    A_3110842("3110842", "교대역 13번"),       // 서초구
    A_3110903("3110903", "강남역 11번"),       // 강남구
    A_3110991("3110991", "잠실역 1번"),        // 송파구 (롯데월드)
    A_3111047("3111047", "강동역 1번");        // 강동구

    private final String code;
    private final String displayName;

    SeoulGuCommercialArea(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    // --- 빠른 조회를 위한 정적(Static) 맵 ---

    // 1. 코드로 Enum 상수를 찾는 맵 (예: "3110903" -> A_3110903)
    private static final Map<String, SeoulGuCommercialArea> BY_CODE =
            Stream.of(values()).collect(Collectors.toMap(SeoulGuCommercialArea::getCode, Function.identity()));

    // 2. 이름으로 Enum 상수를 찾는 맵 (예: "강남역 11번" -> A_3110903)
    private static final Map<String, SeoulGuCommercialArea> BY_DISPLAY_NAME =
            Stream.of(values()).collect(Collectors.toMap(SeoulGuCommercialArea::getDisplayName, Function.identity()));


    // --- 정적 조회 메소드 ---

    /**
     * 상권 코드로 Enum 상수를 조회합니다.
     * @param code (예: "3110903")
     * @return 해당하는 SeoulGuCommercialArea 상수. 없으면 null 반환.
     */
    public static SeoulGuCommercialArea findByCode(String code) {
        return BY_CODE.get(code);
    }

    /**
     * 상권명(displayName)으로 Enum 상수를 조회합니다.
     * @param displayName (예: "강남역 11번")
     * @return 해당하는 SeoulGuCommercialArea 상수. 없으면 null 반환.
     */
    public static SeoulGuCommercialArea findByDisplayName(String displayName) {
        return BY_DISPLAY_NAME.get(displayName);
    }
}