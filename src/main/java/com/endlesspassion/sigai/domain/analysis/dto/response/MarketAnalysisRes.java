package com.endlesspassion.sigai.domain.analysis.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MarketAnalysisRes {

    private Long storeId;
    private String storeName;

    private RevenueComparisonRes revenueComparison; // 매출 비교
    private RankingTrendRes rankingTrend; // 순위 변화 추이
    private CompetitionIndexRes competitionIndex; // 경쟁 강도

    private LocalDate analyzedAt;

    public static MarketAnalysisRes of(
            Long storeId,
            String storeName,
            RevenueComparisonRes revenueComparison,
            RankingTrendRes rankingTrend,
            CompetitionIndexRes competitionIndex
    ) {
        return MarketAnalysisRes.builder()
                .storeId(storeId)
                .storeName(storeName)
                .revenueComparison(revenueComparison)
                .rankingTrend(rankingTrend)
                .competitionIndex(competitionIndex)
                .analyzedAt(LocalDate.now())
                .build();
    }
}
