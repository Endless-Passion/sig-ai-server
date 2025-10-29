package com.endlesspassion.sigai.domain.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store_revenue")
public class StoreRevenue {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "revenue", precision = 15, scale = 2, nullable = false)
    private BigDecimal revenue;

    // 년월 (YYYY형식, 예: "2024")
    @Column(name = "year", nullable = false, length = 6)
    private String year;

    // 년월 (MM 형식, 예: "202401")
    @Column(name = "month", nullable = false, length = 6)
    private String month;

}
