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
    private Double competitionIntensity; // 최근 분기의 경쟁 강도[동일업종 점포수 / 상권 면적(면적 미존재 시 점포수 지표만)]

    public static RevenueComparison of(
            List<QuarterlyRevenueRank> quarterlyRevenueRanks,
            Double competitionIntensity) {
        return RevenueComparison.builder()
                .quarterlyRevenueRanks(quarterlyRevenueRanks)
                .competitionIntensity(competitionIntensity)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class QuarterlyRevenueRank {
        private String quarter; // 분기 코드 (예: "202401" = 2024년 1분기)
        private BigDecimal revenue; // 사용자 매장의 매출액
        private Double topPercentile; // 동일 상권, 동일 업종 내 상위 퍼센트
        private Integer totalStoreCount;  // 전체 매장 수
        private Integer rank; // 순위
        private Integer rankChange; // 전 분기 대비 변화

        public static QuarterlyRevenueRank of(
                String quarter,
                BigDecimal revenue,
                Double topPercentile,
                Integer totalStoreCount,
                Integer rank,
                Integer rankChange) {
            return QuarterlyRevenueRank.builder()
                    .quarter(quarter)
                    .revenue(revenue)
                    .topPercentile(topPercentile)
                    .totalStoreCount(totalStoreCount)
                    .rank(rank)
                    .rankChange(rankChange)
                    .build();
        }


        // 데이터가 없을 때 빈 객체 생성
        public static QuarterlyRevenueRank empty(
                String quarter,
                BigDecimal revenue) {
            return QuarterlyRevenueRank.builder()
                    .quarter(quarter)
                    .revenue(revenue)
                    .topPercentile(null)
                    .totalStoreCount(0)
                    .rank(null)
                    .rankChange(null)
                    .build();
        }
    }
}
