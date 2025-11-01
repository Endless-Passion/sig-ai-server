package com.endlesspassion.sigai.domain.predict.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiPredictRes {

    @JsonProperty("prediction_tier")
    private String predictionTier;

    @JsonProperty("xgb_probability")
    private Double xgbProbability;

    @JsonProperty("rf_probability")
    private Double rfProbability;

    @JsonProperty("threshold_caution")
    private Double thresholdCaution;

    @JsonProperty("threshold_danger")
    private Double thresholdDanger;
}