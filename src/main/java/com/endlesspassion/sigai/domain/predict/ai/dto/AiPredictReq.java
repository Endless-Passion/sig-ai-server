package com.endlesspassion.sigai.domain.predict.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiPredictReq {

    @JsonProperty("TA_YM")
    private Integer taYm;

    @JsonProperty("HPSN_MCT_ZCD_NM")
    private String hpsnMctZcdNm;

    @JsonProperty("MCT_OPE_MS_CN")
    private String mctOpeMsCn;

    @JsonProperty("M12_SME_RY_SAA_PCE_RT")
    private Double m12SmeRySaaPceRt;

    @JsonProperty("M1SME_RY_SAA_RAT")
    private Double m1SmeRySaaRat;

    @JsonProperty("M1_SME_RY_CNT_RAT")
    private Double m1SmeRyCntRat;

    @JsonProperty("M12_SME_RY_ME_MCT_RAT")
    private Double m12SmeRyMeMctRat;

    @JsonProperty("MCT_BRD_NUM")
    private Integer mctBrdNum;

    @JsonProperty("DLV_SAA_RAT")
    private Double dlvSaaRat;

    @JsonProperty("MCT_UE_CLN_REU_RAT")
    private Double mctUeClnReuRat;

    @JsonProperty("RC_M1_SHC_FLP_UE_CLN_RAT")
    private Double rcM1ShcFlpUeClnRat; // 48.0 (fix)

    @JsonProperty("M12_MAL_1020_RAT")
    private Double m12Mal1020Rat;

    @JsonProperty("M12_MAL_30_RAT")
    private Double m12Mal30Rat; // 0.0 (fix)

    @JsonProperty("M12_MAL_40_RAT")
    private Double m12Mal40Rat;

    @JsonProperty("M12_MAL_50_RAT")
    private Double m12Mal50Rat; // 0.0 (fix)

    @JsonProperty("M12_MAL_60_RAT")
    private Double m12Mal60Rat; // 0.0 (fix)
}