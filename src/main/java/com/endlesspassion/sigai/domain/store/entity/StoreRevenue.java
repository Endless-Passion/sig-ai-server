package com.endlesspassion.sigai.domain.store.entity;

import com.endlesspassion.sigai.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_revenue")
public class StoreRevenue extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 매출 정보
    @Column(name = "revenue", precision = 15, scale = 2, nullable = false)
    private BigDecimal monthlyRevenue;

    @Column(name = "delivery_sales_ratio")
    private Float deliverySalesRatio;


    // 고객 통계
    @Column(name = "male_2030_ratio")
    private Float maleCustomer2030Ratio;

    @Column(name = "male_40plus_ratio")
    private Float maleCustomer40PlusRatio;

    @Column(name = "returning_customer_ratio")
    private Float returningCustomerRatio;

    // 년월 (YYYY형식, 예: "2024")
    @Column(name = "year", nullable = false)
    private int year;

    // 년월 (MM 형식, 예: "202401")
    @Column(name = "month", nullable = false)
    private int month;

}
