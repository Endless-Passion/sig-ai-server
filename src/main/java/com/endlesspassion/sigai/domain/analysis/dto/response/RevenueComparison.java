package com.endlesspassion.sigai.domain.analysis.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RevenueComparison {

    private List<QuarterlyRevenueRank> quarterlyRevenueRanks;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuarterlyRevenueRank {
        private String quarter; // 분기 코드 (예: "202401" = 2024년 1분기)
        private BigDecimal revenue; // 사용자 매장의 매출액
        private Double toPercentile; // 동일 상권, 동일 업종 내 상위 퍼센트
        private Integer totalStoreCount;  // 전체 매장 수
        private Integer rank; // 순위
        private Integer rankChange; // 전 분기 대비 변화
    }
}
