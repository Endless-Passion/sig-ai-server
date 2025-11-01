package com.endlesspassion.sigai.domain.predict.client.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangedClose {

    private Float closedRate; // 폐업률(%)
    private Integer rateChange; // 전 분기 대비 폐업률 변화

    public static ChangedClose of(
            Float closedRate,
            Integer rateChange
    ) {
        return ChangedClose.builder()
                .closedRate(closedRate)
                .rateChange(rateChange)
                .build();
    }

    public static ChangedClose empty() {
        return ChangedClose.builder()
                .closedRate(null)
                .rateChange(null)
                .build();
    }
}
