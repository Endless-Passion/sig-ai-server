package com.endlesspassion.sigai.domain.store.dto.request;

import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRevenueReq {

    // 매출 정보 (사장님 입력)
    @DecimalMin(value = "0.0", message = "매출은 0원 이상이어야 합니다.")
    @Digits(integer = 13, fraction = 2, message = "매출은 13자리 정수와 2자리 소수까지 입력 가능합니다.")
    private BigDecimal monthlyRevenue;

    @DecimalMin(value = "0.0", message = "배달 매출 비율은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "배달 매출 비율은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "배달 매출 비율은 소수점 2자리까지 입력 가능합니다.")
    private Float deliverySalesRatio;


    // 고객 통계 (사장님 입력, 비율 단위: 0.0 ~ 100.0)
    @DecimalMin(value = "0.0", message = "고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float maleCustomer2030Ratio;

    @DecimalMin(value = "0.0", message = "고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float maleCustomer40PlusRatio;

    @DecimalMin(value = "0.0", message = "재방문 고객 비중은 0% 이상이어야 합니다.")
    @DecimalMax(value = "100.0", message = "재방문 고객 비중은 100% 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "재방문 고객 비중은 소수점 2자리까지 입력 가능합니다.")
    private Float returningCustomerRatio;

    // 날짜
    @DecimalMin(value = "1950", message = "년은 1950 이상이어야 합니다.")
    @DecimalMax(value = "2100", message = "년은 2100 이하여야 합니다.")
    @Digits(integer = 4, fraction = 0, message = "년은 4자리 숫자로 입력해주세요.")
    private Integer year;

    @DecimalMin(value = "1", message = "월은 1 이상이어야 합니다.")
    @DecimalMax(value = "12", message = "월은 12 이하여야 합니다.")
    @Digits(integer = 2, fraction = 0, message = "월은 1부터 12까지 입력 가능합니다.")
    private Integer month;

    // 가게 ID (필수)
    private Long storeId;

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

    public StoreRevenue to(Store store) {
        return StoreRevenue.builder()
                .store(store)
                .year(this.year)
                .month(this.month)
                .monthlyRevenue(this.monthlyRevenue)
                .deliverySalesRatio(this.deliverySalesRatio)
                .maleCustomer2030Ratio(this.maleCustomer2030Ratio)
                .maleCustomer40PlusRatio(this.maleCustomer40PlusRatio)
                .returningCustomerRatio(this.returningCustomerRatio)
                .build();
    }
}
