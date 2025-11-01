package com.endlesspassion.sigai.domain.predict.client.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangedRevenue {

    private Integer rankChange; // 전 분기 대비 순위 변화
    private Float percentileChange; // 전 분기 대비 비율 변화
    private Integer countChange; // 전 분기 대비 건수 변화


    public static ChangedRevenue of(
            Integer rankChange,
            Float percentileChange,
            Integer countChange
    ) {
        return ChangedRevenue.builder()
                .rankChange(rankChange)
                .percentileChange(percentileChange)
                .countChange(countChange)
                .build();
    }

    public static ChangedRevenue empty() {
        return ChangedRevenue.builder()
                .rankChange(null)
                .percentileChange(null)
                .countChange(null)
                .build();
    }
}