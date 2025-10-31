package com.endlesspassion.sigai.domain.analysis.dto.request;

import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 상권 분석 요청 DTO
 *
 * 검증 전략:
 * - Bean Validation을 활용한 입력값 검증
 * - 컨트롤러 레벨에서 @Valid로 자동 검증
 * - 비즈니스 로직 진입 전 데이터 무결성 보장
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MerkeyAnalysisReq {

    @NotNull(message = "가게 ID는 필수입니다.")
    private Long storeId; // 가게 ID

    @NotNull(message = "업종 코드는 필수입니다.")
    private ServiceIndustry serviceIndustry; // 업종 코드

    @NotNull(message = "분기는 필수입니다.")
    @Pattern(regexp = "^\\d{4}(0[1-4])$", message = "분기는 YYYYQQ 형식 (예: 202401)")
    private String quarter; // 현재 분기

    @Min(value = 1, message = "최소 1개 분기 이상 선택해야 합니다.")
    @Max(value = 20, message = "최대 20개 분기까지 선택해야 합니다.")
    @Builder.Default
    private int count = 8; // 보여줄 분기 수

    /**
     * 검증: 분기가 미래인지 아닌지 확인
     * @param currentQuarter 현재 시스템 분기
     * @return 유효하면 ture
     */

    public boolean isValidQuarter(String currentQuarter) {
        return this.quarter.compareTo(currentQuarter) <= 0;
    }
}
