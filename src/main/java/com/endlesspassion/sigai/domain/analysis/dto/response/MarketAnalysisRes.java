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

    private RevenueComparison revenueComparison; // 동일 상권 내 매출 비교
    private PopulationComparison populationComparison; // 동일 상권 내 인구 데이터 비교(이건 빈껍데기만 응답 예정)
    private ClosedComparison closedComparison; // 동일 상권 내 폐업률 비교

    private LocalDate analyzedAt;

    public static MarketAnalysisRes of(
            Long storeId,
            String storeName,
            RevenueComparison revenueComparison,
            PopulationComparison populationComparison,
            ClosedComparison closedComparison
    ) {
        return MarketAnalysisRes.builder()
                .storeId(storeId)
                .storeName(storeName)
                .revenueComparison(revenueComparison)
                .populationComparison(populationComparison)
                .closedComparison(closedComparison)
                .analyzedAt(LocalDate.now())
                .build();
    }
}
