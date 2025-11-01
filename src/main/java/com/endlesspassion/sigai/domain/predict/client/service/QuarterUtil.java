package com.endlesspassion.sigai.domain.predict.client.service; // [수정] 세미콜론 추가

import org.springframework.stereotype.Component;

@Component
public class QuarterUtil {

    /**
     * "YYYYQ" 형식의 현재 분기를 "YYYYQ-1" 형식의 이전 분기로 변환합니다.
     * (예: "20241" -> "20234", "20242" -> "20241")
     *
     * @param quarter (예: "20241")
     * @return 이전 분기 문자열 (예: "20234")
     */
    public String getPreviousQuarter(String quarter) {
        if (quarter == null || quarter.length() != 5) {
            throw new IllegalArgumentException("분기 형식은 YYYYQ (예: 20241)이어야 합니다.");
        }
        int year = Integer.parseInt(quarter.substring(0, 4));
        int q = Integer.parseInt(quarter.substring(4));

        if (q == 1) {
            return (year - 1) + "4";
        } else {
            return year + String.valueOf(q - 1);
        }
    }

    /**
     * "YYYYQ" 형식의 분기를 AI가 요구하는 YYYYMM 형식 (분기의 마지막 월)으로 변환합니다.
     * AI 스펙 주석의 "202410"은 "2024년 1분기" -> "202403" (1분기 마지막 월)의 오타로 간주하고 구현합니다.
     * (예: "20241" -> 202403, "20244" -> 202412)
     *
     * @param quarter (예: "20241")
     * @return YYYYMM 형식의 정수 (예: 202403)
     */
    public Integer convertQuarterToAiMonth(String quarter) {
        if (quarter == null || quarter.length() != 5) {
            throw new IllegalArgumentException("분기 형식은 YYYYQ (예: 20241)이어야 합니다.");
        }
        String year = quarter.substring(0, 4);
        int q = Integer.parseInt(quarter.substring(4));

        String month;
        switch (q) {
            case 1: month = "03"; break; // 1Q -> 3월
            case 2: month = "06"; break; // 2Q -> 6월
            case 3: month = "09"; break; // 3Q -> 9월
            case 4: month = "12"; break; // 4Q -> 12월
            default:
                throw new IllegalArgumentException("잘못된 분기 번호입니다: " + q);
        }
        return Integer.parseInt(year + month);
    }
}