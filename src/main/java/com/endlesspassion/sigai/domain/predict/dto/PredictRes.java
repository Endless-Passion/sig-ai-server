package com.endlesspassion.sigai.domain.predict.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredictRes {

    private String predictionTier; // "주의" | "위험" | "안정"
    private Double xgbProbability; // 주위 or 위험일 때 위험도
    private Double rfProbability; // 안정일 때 위험도
    private Double thresholdCaution; // 주의 판단 기준 임계값
    private Double thresholdDanger; // 위험 판단 기준 임계값

    private ChangedRevenue changedRevenue;
    private ChangedClose changeClose;

    public static PredictRes of(
            String predictionTier,
            Double xgbProbability,
            Double rfProbability,
            Double thresholdCaution,
            Double thresholdDanger,
            ChangedRevenue changedRevenue,
            ChangedClose changedClose
    ) {
        return PredictRes.builder()
                .predictionTier(predictionTier)
                .xgbProbability(xgbProbability)
                .rfProbability(rfProbability)
                .thresholdCaution(thresholdCaution)
                .thresholdDanger(thresholdDanger)
                .changedRevenue(changedRevenue)
                .changeClose(changedClose)
                .build();
    }

}
