package com.endlesspassion.sigai.domain.store.dto.respose;

import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 월별 가게 매출 응답 DTO
 * StoreRevenue 엔티티 리스트를 응답 DTO로 변환
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRevenueRes {

    /**
     * 월별 매출 데이터 리스트
     * 최근 월부터 과거 순으로 정렬
     */
    private List<MonthlyRevenue> revenues;

    /**
     * 월별 매출 상세 정보
     */
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MonthlyRevenue {
        private Integer year;              // 년도
        private Integer month;             // 월
        private BigDecimal monthlyRevenue; // 월 매출액

        // 매출 정보
        private Float deliverySalesRatio;  // 배달 매출 비율(%)

        // 고객 통계
        private Float maleCustomer2030Ratio;    // 남성 2030대 고객 비중(%)
        private Float maleCustomer40PlusRatio;  // 남성 40대 이상 고객 비중(%)
        private Float returningCustomerRatio;   // 재방문 고객 비중(%)

        /**
         * StoreRevenue 엔티티를 MonthlyRevenue DTO로 변환
         */
        public static MonthlyRevenue from(StoreRevenue storeRevenue) {
            return MonthlyRevenue.builder()
                    .year(storeRevenue.getYear())
                    .month(storeRevenue.getMonth())
                    .monthlyRevenue(storeRevenue.getMonthlyRevenue())
                    .deliverySalesRatio(storeRevenue.getDeliverySalesRatio())
                    .maleCustomer2030Ratio(storeRevenue.getMaleCustomer2030Ratio())
                    .maleCustomer40PlusRatio(storeRevenue.getMaleCustomer40PlusRatio())
                    .returningCustomerRatio(storeRevenue.getReturningCustomerRatio())
                    .build();
        }

        /**
         * 데이터가 없는 경우 빈 객체 생성
         */
        public static MonthlyRevenue empty(Integer year, Integer month) {
            return MonthlyRevenue.builder()
                    .year(year)
                    .month(month)
                    .monthlyRevenue(null)
                    .deliverySalesRatio(null)
                    .maleCustomer2030Ratio(null)
                    .maleCustomer40PlusRatio(null)
                    .returningCustomerRatio(null)
                    .build();
        }
    }

    /**
     * StoreRevenue 엔티티 리스트를 StoreRevenueRes DTO로 변환
     */
    public static StoreRevenueRes from(List<StoreRevenue> storeRevenues) {
        List<MonthlyRevenue> monthlyRevenues = storeRevenues.stream()
                .map(MonthlyRevenue::from)
                .collect(Collectors.toList());

        return StoreRevenueRes.builder()
                .revenues(monthlyRevenues)
                .build();
    }
}
