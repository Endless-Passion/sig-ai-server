package com.endlesspassion.sigai.domain.predict.ai.dto;

import com.endlesspassion.sigai.domain.predict.client.dto.PredictReq;
import com.endlesspassion.sigai.domain.predict.client.service.QuarterUtil;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustryAiMap; // AI 업종 매핑 Enum
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiPredictReq {

    // ... (JsonProperty 필드 선언부) ...
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
    private Double rcM1ShcFlpUeClnRat;
    @JsonProperty("M12_MAL_1020_RAT")
    private Double m12Mal1020Rat;
    @JsonProperty("M12_MAL_30_RAT")
    private Double m12Mal30Rat;
    @JsonProperty("M12_MAL_40_RAT")
    private Double m12Mal40Rat;
    @JsonProperty("M12_MAL_50_RAT")
    private Double m12Mal50Rat;
    @JsonProperty("M12_MAL_60_RAT")
    private Double m12Mal60Rat;


    /**
     * PredictService에서 조회한 모든 데이터를 AI 요청 DTO로 변환
     */
    public static AiPredictReq to(
            PredictReq req,
            Store store,
            StoreRevenue revenue,
            Double publicRank, // AI 매출 순위
            Double publicRatio, // AI 매출 비율
            Double publicCountRatio, // AI 매출 건수 비율
            Double publicCloseRatio, // AI 폐업 비율
            QuarterUtil quarterUtil // 분기 변환기
    ) {
        // --- 1. 기본 정보 변환 ---
        // (가정) QuarterUtil에 "20241" -> 202410 (AI가 원하는 YYYYMM 형식) 변환 로직
        Integer taYm = quarterUtil.convertQuarterToAiMonth(req.getQuarter());
        String aiIndustryName = ServiceIndustryAiMap.getAiCategoryByCode(store.getServiceIndustry().getCode());
        String operationMonths = req.getMonthsOfOperation() + "개월";

        // --- 2. 사장님 입력값 (0~100) -> AI 요구값 (0~1) ---
        // (null일 경우 AI 요청 스펙의 기본값 사용)
        Double deliveryRatio = (revenue.getDeliverySalesRatio() != null) ? revenue.getDeliverySalesRatio().doubleValue() / 100.0 : 0.0;
        Double returningRatio = (revenue.getReturningCustomerRatio() != null) ? revenue.getReturningCustomerRatio().doubleValue() / 100.0 : 0.1;
        Double male2030Ratio = (revenue.getMaleCustomer2030Ratio() != null) ? revenue.getMaleCustomer2030Ratio().doubleValue() / 100.0 : 0.1;
        Double male40PlusRatio = (revenue.getMaleCustomer40PlusRatio() != null) ? revenue.getMaleCustomer40PlusRatio().doubleValue() / 100.0 : 0.0;

        return AiPredictReq.builder()
                // --- 1. 기본 정보 ---
                .taYm(taYm)
                .hpsnMctZcdNm(aiIndustryName)
                .mctOpeMsCn(operationMonths)

                // --- 2. Public Data (헬퍼 함수에서 추출) ---
                .m12SmeRySaaPceRt(publicRank)
                .m1SmeRySaaRat(publicRatio)
                .m1SmeRyCntRat(publicCountRatio)
                .m12SmeRyMeMctRat(publicCloseRatio)

                // --- 3. Store Data ---
                .mctBrdNum(store.getIsFranchise() ? 1 : null)

                // --- 4. StoreRevenue Data (비율 변환) ---
                .dlvSaaRat(deliveryRatio)
                .mctUeClnReuRat(returningRatio)
                .m12Mal1020Rat(male2030Ratio)
                .m12Mal40Rat(male40PlusRatio)

                // --- 5. 고정값 (FIX) ---
                .rcM1ShcFlpUeClnRat(0.05) // 요청 스펙의 0.05
                .m12Mal30Rat(0.0)
                .m12Mal50Rat(0.0)
                .m12Mal60Rat(0.0)
                .build();
    }
}