package com.endlesspassion.sigai.domain.store.dto.respose;

import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRes {

    private Long id;

    private String storeName;

    private ServiceIndustry serviceIndustry;

    private String gu;

    private String dong;

    private LocalDate openingDate;

    private Boolean isFranchise;

    public static StoreRes of(Store store) {
        return StoreRes.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .serviceIndustry(store.getServiceIndustry())
                .gu(store.getGu())
                .dong(store.getDong())
                .openingDate(store.getOpeningDate())
                .isFranchise(store.getIsFranchise())
                .build();
    }
}
