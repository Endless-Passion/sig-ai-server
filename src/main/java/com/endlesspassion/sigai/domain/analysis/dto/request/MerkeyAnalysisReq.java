package com.endlesspassion.sigai.domain.analysis.dto.request;

import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MerkeyAnalysisReq {

    private Long storeId; // 가게 ID
    private ServiceIndustry serviceIndustry; // 업종 코드
    private String quarter; // 현재 분기
    private int count; // 보여줄 분기 수



}
