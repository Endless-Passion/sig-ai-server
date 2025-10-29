package com.endlesspassion.sigai.domain.store.dto;

import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 가게 정보 등록/수정 요청 DTO (사장님 입력 기반)
 * 1. 사장님 직접 입력: 고객 통계, 매출 정보, 기본 정보
 * 2. 시스템 생성: 기준년월, 동일업종 통계
 * 3. AI 서버 계산: 업종별 분석, 예측 데이터
 * 원본 CSV 매핑:
 * - M12_MAL_1020_RAT → maleCustomer2030Ratio
 * - M12_MAL_30_RAT → maleCustomer40PlusRatio
 * - MCT_UE_CLN_REU_RAT → returningCustomerRatio
 * - RC_M1_SHC_FLP_UE_CLN_RAT → floatingPopulationRatio
 * - MCT_BRD_NUM → brandCode
 * - DLV_SAA_RAT → deliverySalesRatio
 * - MCT_BSE_AR → storeAddress
 * - HPSN_MCT_ZCD_NM → serviceIndustry
 * - ARE_D → openingDate
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReq {

    // ============================================================
    // 기본 정보 (사장님 필수 입력)
    // ============================================================

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 100, message = "가게 이름은 100자 이내로 입력해주세요.")
    private String storeName;

    @NotNull(message = "업종은 필수입니다.")
    private ServiceIndustry serviceIndustry;

    @Size(max = 50, message = "구명은 50자 이내로 입력해주세요.")
    private String gu;  // MCT_SIGUNGU_NM (예: "성동구")

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 50, message = "주소는 200자 이내로 입력해주세요.")
    private String dong;  // MCT_BSE_AR (예: "성수동")


    @NotNull(message = "개설일은 필수입니다.")
    private LocalDate openingDate;  // ARE_D (YYYYMMDD → LocalDate 변환)

    // 프랜차이즈 정보 (사장님 입력)
    @Min(value = 0, message = "브랜드 코드는 0 또는 1이어야 합니다.")
    @Max(value = 1, message = "브랜드 코드는 0 또는 1이어야 합니다.")
    private Integer brandCode;  // 1: 프랜차이즈, 0: 일반

    // 고객 통계 (사장님 입력, 비율 단위: 0.0 ~ 100.0)

    /**
     * 남성 2030대 고객 비중 (%)
     * M12_MAL_1020_RAT
     * 예시:
     * - 25.5 → 전체 고객 중 25.5%가 남성 2030대
     * - 0.0 ~ 100.0 사이의 값
     */
    @DecimalMin(value = "0.0", message = "고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float maleCustomer2030Ratio;

    /**
     * 남성 40대 이상 고객 비중 (%)
     * M12_MAL_30_RAT
     */
    @DecimalMin(value = "0.0", message = "고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float maleCustomer40PlusRatio;

    /**
     * 재방문 고객 비중 (%)
     * MCT_UE_CLN_REU_RAT
     * 정의: 전체 고객 중 재방문한 고객의 비율
     * 높을수록 충성도가 높은 가게
     */
    @DecimalMin(value = "0.0", message = "재방문 고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "재방문 고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "재방문 고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float returningCustomerRatio;

    /**
     * 유동인구 고객 비중 (%)
     * RC_M1_SHC_FLP_UE_CLN_RAT
     * 정의: 전체 고객 중 유동인구(거주지가 아닌 방문객)의 비율
     * 높을수록 상권 의존도가 높음
     */
    @DecimalMin(value = "0.0", message = "유동인구 고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "유동인구 고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "유동인구 고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float floatingPopulationRatio;

    // 매출 정보 (사장님 입력)
    /**
     * 배달 매출 비율 (%)
     * DLV_SAA_RAT
     * 정의: 전체 매출 중 배달 매출의 비율
     * 예시:
     * - 30.0 → 전체 매출의 30%가 배달 매출
     * - 0.0 → 배달 없음 (홀 매출만)
     * - 100.0 → 배달 전문점
     */
    @DecimalMin(value = "0.0", message = "배달 매출 비율은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "배달 매출 비율은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "배달 매출 비율은 소수점 2자리까지 입력 가능합니다.")
    private Float deliverySalesRatio;

    /**
     * 월 평균 매출액 (원)
     * BigDecimal 사용 이유:
     * - 금융 데이터는 정밀한 연산 필요
     * - double은 부동소수점 오차 발생
     */
    @DecimalMin(value = "0.0", message = "매출은 0원 이상이어야 합니다.")
    @Digits(integer = 13, fraction = 2, message = "매출은 13자리 정수와 2자리 소수까지 입력 가능합니다.")
    private BigDecimal monthlyRevenue;

    @Min(value = 0, message = "고객 수는 0명 이상이어야 합니다.")
    @Max(value = 100000, message = "고객 수는 100,000명 이하로 입력해주세요.")
    private Integer dailyCustomerCount;

    // ============================================================
    // Builder
    // ============================================================

    @Builder
    public StoreReq(String storeName, ServiceIndustry serviceIndustry,
                    String gu, String dong,
                    LocalDate openingDate, Integer brandCode,
                    Float maleCustomer2030Ratio, Float maleCustomer40PlusRatio,
                    Float returningCustomerRatio, Float floatingPopulationRatio,
                    Float deliverySalesRatio, BigDecimal monthlyRevenue,
                    Integer dailyCustomerCount) {
        this.storeName = storeName;
        this.serviceIndustry = serviceIndustry;
        this.gu = gu;
        this.dong = dong;
        this.openingDate = openingDate;
        this.brandCode = brandCode;
        this.maleCustomer2030Ratio = maleCustomer2030Ratio;
        this.maleCustomer40PlusRatio = maleCustomer40PlusRatio;
        this.returningCustomerRatio = returningCustomerRatio;
        this.floatingPopulationRatio = floatingPopulationRatio;
        this.deliverySalesRatio = deliverySalesRatio;
        this.monthlyRevenue = monthlyRevenue;
        this.dailyCustomerCount = dailyCustomerCount;
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    /**
     * 프랜차이즈 여부 확인
     */
    public boolean isFranchise() {
        return brandCode != null && brandCode == 1;
    }

    /**
     * DTO → Entity 변환
     *
     * 참고: 동일업종 통계는 Service Layer에서 계산 후 설정
     */
    public Store toEntity() {
        return Store.builder()
                .storeName(this.storeName)
                .serviceIndustry(this.serviceIndustry)
                .gu(this.gu)
                .dong(this.dong)
                .openingDate(this.openingDate)
                .isFranchise(this.isFranchise())
                .maleCustomer2030Ratio(this.maleCustomer2030Ratio)
                .maleCustomer40PlusRatio(this.maleCustomer40PlusRatio)
                .returningCustomerRatio(this.returningCustomerRatio)
                .floatingPopulationRatio(this.floatingPopulationRatio)
                .deliverySalesRatio(this.deliverySalesRatio)
                .monthlyRevenue(this.monthlyRevenue)
                .dailyCustomerCount(this.dailyCustomerCount)
                .build();
    }

    /**
     * 비즈니스 검증: 고객 비중 합계 체크
     *
     * 주의: 남성 2030대 + 남성 40대 이상이 100% 넘으면 안 됨
     * (여성, 기타 연령대도 포함되므로)
     */
    public void validateCustomerRatios() {
        if (maleCustomer2030Ratio != null && maleCustomer40PlusRatio != null) {
            float sum = maleCustomer2030Ratio + maleCustomer40PlusRatio;
            if (sum > 100.0f) {
                throw new IllegalArgumentException(
                        String.format("남성 고객 비중 합계(%.2f%%)가 100%%를 초과할 수 없습니다.", sum)
                );
            }
        }
    }
}