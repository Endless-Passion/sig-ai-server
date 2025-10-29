package com.endlesspassion.sigai.global.common.enums;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 공공데이터 업종 코드(CS코드)를 AI 모델이 요구하는 특정 카테고리로 매핑하는 Enum.
 * - getAiCategoryByCode("CS100001") -> "백반/가정식"
 * - getAiCategoryByOriginalName("한식음식점") -> "백반/가정식"
 */
@Getter
public enum ServiceIndustryAiMap {

    // CS100000: 외식업
    CS100001("CS100001", "한식음식점", "백반/가정식"), // '한식음식점'이 포괄적이므로 '백반/가정식'으로 매핑
    CS100002("CS100002", "중식음식점", "중식당"),
    CS100003("CS100003", "일식음식점", "일식당"),
    CS100004("CS100004", "양식음식점", "일반양식"),
    CS100005("CS100005", "제과점", "베이커리"),
    CS100006("CS100006", "패스트푸드점", "햄버거"), // '패스트푸드'가 없으므로 대표격인 '햄버거'로 매핑
    CS100007("CS100007", "치킨전문점", "치킨"),
    CS100008("CS100008", "분식전문점", "분식"),
    CS100009("CS100009", "호프-간이주점", "호프/맥주"),
    CS100010("CS100010", "커피-음료", "카페"),

    // CS200000: 서비스업 (대부분 AI 카테고리에 해당 없음)
    CS200001("CS200001", "일반교습학원", "업종_기타"),
    CS200002("CS200002", "외국어학원", "업종_기타"),
    CS200003("CS200003", "예술학원", "업종_기타"),
    CS200005("CS200005", "스포츠 강습", "업종_기타"),
    CS200006("CS200006", "일반의원", "업종_기타"),
    CS200007("CS200007", "치과의원", "업종_기타"),
    CS200008("CS200008", "한의원", "업종_기타"),
    CS200016("CS200016", "당구장", "업종_기타"),
    CS200017("CS200017", "골프연습장", "업종_기타"),
    CS200019("CS200019", "PC방", "업종_기타"),
    CS200024("CS200024", "스포츠클럽", "업종_기타"),
    CS200025("CS200025", "자동차수리", "업종_기타"),
    CS200026("CS200026", "자동차미용", "업종_기타"),
    CS200028("CS200028", "미용실", "업종_기타"),
    CS200029("CS200029", "네일숍", "업종_기타"),
    CS200030("CS200030", "피부관리실", "업종_기타"),
    CS200031("CS200031", "세탁소", "업종_기타"),
    CS200032("CS200032", "가전제품수리", "업종_기타"),
    CS200033("CS200033", "부동산중개업", "업종_기타"),
    CS200034("CS200034", "여관", "업종_기타"),
    CS200036("CS200036", "고시원", "업종_기타"),
    CS200037("CS200037", "노래방", "업종_기타"),

    // CS300000: 소매업 (일부 식료품 매핑)
    CS300001("CS300001", "슈퍼마켓", "식료품"),
    CS300002("CS300002", "편의점", "식료품"),
    CS300003("CS300003", "컴퓨터및주변장치판매", "업종_기타"),
    CS300004("CS300004", "핸드폰", "업종_기타"),
    CS300006("CS300006", "미곡판매", "미곡상"),
    CS300007("CS300007", "육류판매", "축산물"),
    CS300008("CS300008", "수산물판매", "수산물"),
    CS300009("CS300009", "청과상", "청과물"),
    CS300010("CS300010", "반찬가게", "반찬"),
    CS300011("CS300011", "일반의류", "업종_기타"),
    CS300014("CS300014", "신발", "업종_기타"),
    CS300015("CS300015", "가방", "업종_기타"),
    CS300016("CS300016", "안경", "업종_기타"),
    CS300017("CS300017", "시계및귀금속", "업종_기타"),
    CS300018("CS300018", "의약품", "업종_기타"),
    CS300019("CS300019", "의료기기", "업종_기타"),
    CS300020("CS300020", "서적", "업종_기타"),
    CS300021("CS300021", "문구", "업종_기타"),
    CS300022("CS300022", "화장품", "업종_기타"),
    CS300024("CS300024", "운동/경기용품", "업종_기타"),
    CS300025("CS300025", "자전거 및 기타운송장비", "업종_기타"),
    CS300026("CS300026", "완구", "업종_기타"),
    CS300027("CS300027", "섬유제품", "업종_기타"),
    CS300028("CS300028", "화초", "농산물"), // '화초'는 '농산물'로 매핑
    CS300029("CS300029", "애완동물", "업종_기타"),
    CS300031("CS300031", "가구", "업종_기타"),
    CS300032("CS300032", "가전제품", "업종_기타"),
    CS300033("CS300033", "철물점", "업종_기타"),
    CS300035("CS300035", "인테리어", "업종_기타"),
    CS300036("CS300036", "조명용품", "업종_기타"),
    CS300043("CS300043", "전자상거래업", "업종_기타");

    private final String originalCode;
    private final String originalDisplayName;
    private final String aiCategory;

    // --- Enum 생성자 및 기본 Getter ---

    ServiceIndustryAiMap(String originalCode, String originalDisplayName, String aiCategory) {
        this.originalCode = originalCode;
        this.originalDisplayName = originalDisplayName;
        this.aiCategory = aiCategory;
    }



    // --- 빠른 조회를 위한 정적(Static) 맵 ---

    // 1. 원본 코드로 Enum 상수를 찾는 맵 (예: "CS100001" -> CS100001)
    private static final Map<String, ServiceIndustryAiMap> BY_CODE =
            Stream.of(values()).collect(Collectors.toMap(ServiceIndustryAiMap::getOriginalCode, Function.identity()));

    // 2. 원본 이름으로 Enum 상수를 찾는 맵 (예: "한식음식점" -> CS100001)
    private static final Map<String, ServiceIndustryAiMap> BY_ORIGINAL_NAME =
            Stream.of(values()).collect(Collectors.toMap(ServiceIndustryAiMap::getOriginalDisplayName, Function.identity()));


    // --- 핵심 매핑 메소드 (요청사항) ---

    /**
     * 공공데이터 '업종 코드'로 'AI 카테고리'를 조회합니다.
     * @param code (예: "CS100001")
     * @return 매핑되는 AI 카테고리 (예: "백반/가정식"). 만약 일치하는 코드가 없으면 "업종_기타" 반환.
     */
    public static String getAiCategoryByCode(String code) {
        ServiceIndustryAiMap mapping = BY_CODE.get(code);
        if (mapping != null) {
            return mapping.getAiCategory();
        }
        return "업종_기타"; // 일치하는 코드가 없을 경우
    }

    /**
     * 공공데이터 '업종명'으로 'AI 카테고리'를 조회합니다.
     * @param originalDisplayName (예: "한식음식점")
     * @return 매핑되는 AI 카테고리 (예: "백반/가정식"). 만약 일치하는 이름이 없으면 "업종_기타" 반환.
     */
    public static String getAiCategoryByOriginalName(String originalDisplayName) {
        ServiceIndustryAiMap mapping = BY_ORIGINAL_NAME.get(originalDisplayName);
        if (mapping != null) {
            return mapping.getAiCategory();
        }
        return "업종_기타"; // 일치하는 이름이 없을 경우
    }
}