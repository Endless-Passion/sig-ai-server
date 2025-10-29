package com.endlesspassion.sigai.domain.store.entity;


import com.endlesspassion.sigai.domain.store.dto.StoreReq;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 생성/수정 시간 자동화
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    // --- 1. 사장님 입력 정보 (StoreReq DTO 기반) ---

    @Column(nullable = false, length = 100)
    private String storeName;

    @Enumerated(EnumType.STRING) // Enum 이름을 문자열로 저장 (예: CS100001)
    @Column(nullable = false, length = 20)
    private ServiceIndustry serviceIndustry;

    @Column(nullable = false, length = 50)
    private String gu; // 시군구명 (예: "성동구")

    @Column(nullable = false, length = 50)
    private String dong; // 행정동명 (예: "성수동")

    @Column(nullable = false)
    private LocalDate openingDate;

    @Column(nullable = false)
    private boolean isFranchise; // 1: true, 0: false

    // 고객 통계
    @Column(name = "male_2030_ratio")
    private Float maleCustomer2030Ratio;

    @Column(name = "male_40plus_ratio")
    private Float maleCustomer40PlusRatio;

    @Column(name = "returning_customer_ratio")
    private Float returningCustomerRatio;

    @Column(name = "floating_pop_ratio")
    private Float floatingPopulationRatio;

    // 매출 정보
    @Column(name = "delivery_sales_ratio")
    private Float deliverySalesRatio;

    @Column(precision = 15, scale = 2) // 예: 999,999,999,999.99 (13자리 정수 + 2자리 소수)
    private BigDecimal monthlyRevenue;

    private Integer dailyCustomerCount;

    // --- 2. 시스템/서비스 설정 정보 (DTO에 없는 필드) ---
    // 생성자에서 제외되어 null로 초기화됨

    @Column(precision = 15, scale = 2)
    private BigDecimal industryAvgRevenue;

    @Column(name = "industry_avg_delivery_ratio")
    private Float industryAvgDeliverySalesRatio;

    // --- 3. AI 분석/예측 데이터 (DTO에 없는 필드) ---
    // 생성자에서 제외되어 null로 초기화됨

    @Column(precision = 15, scale = 2)
    private BigDecimal predictedRevenue;

    @Lob
    private String aiAnalysisReport;


    // --- 4. 기타 연관관계 (주석 처리) ---

    // // 예: 가게 주인(사장님)과의 연관관계
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;


    // --- 5. 생성/수정 시간 ---

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- 7. 비즈니스 로직 (업데이트) ---

    /**
     * 가게 정보 수정 (사장님 입력 기반)
     */
    public void update(StoreReq dto) {
        this.storeName = dto.getStoreName();
        this.serviceIndustry = dto.getServiceIndustry();
        this.gu = dto.getGu();
        this.dong = dto.getDong(); // this.dongName이 아닌 this.dong에 매핑 (수정됨)
        this.openingDate = dto.getOpeningDate();
        this.isFranchise = dto.isFranchise();
        this.maleCustomer2030Ratio = dto.getMaleCustomer2030Ratio();
        this.maleCustomer40PlusRatio = dto.getMaleCustomer40PlusRatio();
        this.returningCustomerRatio = dto.getReturningCustomerRatio();
        this.floatingPopulationRatio = dto.getFloatingPopulationRatio();
        this.deliverySalesRatio = dto.getDeliverySalesRatio();
        this.monthlyRevenue = dto.getMonthlyRevenue();
        this.dailyCustomerCount = dto.getDailyCustomerCount();
    }
}