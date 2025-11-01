package com.endlesspassion.sigai.domain.store.entity;

import com.endlesspassion.sigai.domain.member.entity.Member;
import com.endlesspassion.sigai.domain.store.dto.request.StoreReq;
import com.endlesspassion.sigai.global.common.entity.BaseTimeEntity;
import com.endlesspassion.sigai.global.common.enums.ServiceArea;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Enumerated(EnumType.STRING) // Enum 이름을 문자열로 저장 (예: 공릉동 국수거리)
    @Column(nullable = false, length = 20)
    private ServiceArea serviceArea;

    @Enumerated(EnumType.STRING) // Enum 이름을 문자열로 저장 (예: 한식)
    @Column(nullable = false, length = 20)
    private ServiceIndustry serviceIndustry;

    @Column(nullable = false, length = 50)
    private String gu; // 시군구명 (예: "성동구")

    @Column(nullable = false, length = 50)
    private String dong; // 행정동명 (예: "성수동")

    @Column(nullable = false)
    private LocalDate openingDate;

    @Column(nullable = false)
    private Boolean isFranchise; // 1: true, 0: false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(StoreReq dto) {
        this.storeName = dto.getStoreName();
        this.serviceArea = dto.getServiceArea();
        this.serviceIndustry = dto.getServiceIndustry();
        this.gu = dto.getGu();
        this.dong = dto.getDong();
        this.openingDate = dto.getOpeningDate();
        this.isFranchise = dto.isFranchise();
    }
}