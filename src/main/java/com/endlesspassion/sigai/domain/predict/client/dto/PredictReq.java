package com.endlesspassion.sigai.domain.predict.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredictReq {

    @NotNull(message = "가맹점 ID는 필수입니다.")
    private Long storeId;

    @NotBlank(message = "현재 분기는 필수입니다.")
    private String quarter;

    @NotNull(message = "운영 개월 수는 필수입니다.")
    private Integer monthsOfOperation;
}
