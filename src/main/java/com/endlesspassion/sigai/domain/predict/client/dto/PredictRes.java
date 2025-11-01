package com.endlesspassion.sigai.domain.predict.client.dto;

import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictRes;
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

    private ChangedRevenue changedRevenue; // 전분기 대비 매출 변화
    private ChangedClose changeClose; // 전분기 대비 폐업 변화 (필드명 'changeClose' 유지)

    /**
     * (기존) 모든 필드를 수동으로 받는 팩토리 메소드
     */
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
                .changeClose(changedClose) // 필드명 'changeClose'
                .build();
    }

    public static PredictRes of(
            AiPredictRes aiPredictRes,
            ChangedRevenue changedRevenue,
            ChangedClose changedClose
    ) {
        return PredictRes.builder()
                // --- AI 서버 응답 ---
                .predictionTier(aiPredictRes.getPredictionTier())
                .xgbProbability(aiPredictRes.getXgbProbability())
                .rfProbability(aiPredictRes.getRfProbability())
                .thresholdCaution(aiPredictRes.getThresholdCaution())
                .thresholdDanger(aiPredictRes.getThresholdDanger())

                // --- 내부 분석 결과 (전 분기 대비) ---
                .changedRevenue(changedRevenue)
                .changeClose(changedClose)
                .build();
    }

}