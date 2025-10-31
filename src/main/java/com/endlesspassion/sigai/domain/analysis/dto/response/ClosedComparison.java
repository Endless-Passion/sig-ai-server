package com.endlesspassion.sigai.domain.analysis.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ClosedComparison {

    /**
     * 분기별 폐업률 데이터 리스트
     * 최근 분기부터 과거 순으로 정렬
     */
    private List<QuarterlyClosedRates> quarterlyClosedRates;
    private Double averageClosedRate; // 평균 폐업률(전체 분기 평균)
    private String trend; // 폐업률 추세(증가/감소/유지) "INCREASING", "DECREASING", "STABLE": 시간 나면 Enum Mapping

    // 분기별 폐업률 상세 정보
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuarterlyClosedRates {

        private String quarter; // 분기 코드
        private Double closedRate; // 폐업률(%)
        private Integer closedStoreCount; // 폐업 점포 수
        private Integer totalStoreCount; // 전체 점포 수
        private Double rateChange; // 전 분기 대비 폐업률 변화(% point): 양수 증가, 음수 감소

    }
}
