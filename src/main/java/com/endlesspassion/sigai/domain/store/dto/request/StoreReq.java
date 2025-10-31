package com.endlesspassion.sigai.domain.store.dto.request;

import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReq {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 100, message = "가게 이름은 100자 이내로 입력해주세요.")
    private String storeName;

    @NotNull(message = "업종은 필수입니다.")
    private ServiceIndustry serviceIndustry;

    @Size(max = 50, message = "구명은 50자 이내로 입력해주세요.")
    private String gu;  // MCT_SIGUNGU_NM (예: "성동구")

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 50, message = "주소는 200자 이내로 입력해주세요.")
    private String dong;  // MCT_BSE_AR (예: "성수동")


    @NotNull(message = "개설일은 필수입니다.")
    private LocalDate openingDate;  // ARE_D (YYYYMMDD → LocalDate 변환)

    // 프랜차이즈 정보 (사장님 입력)
    @Min(value = 0, message = "브랜드 코드는 0 또는 1이어야 합니다.")
    @Max(value = 1, message = "브랜드 코드는 0 또는 1이어야 합니다.")
    private Integer brandCode;  // 1: 프랜차이즈, 0: 일반

    public Boolean isFranchise() {
        return brandCode != null && brandCode == 1;
    }

    public Store to() {
        return Store.builder()
                .storeName(this.storeName)
                .serviceIndustry(this.serviceIndustry)
                .gu(this.gu)
                .dong(this.dong)
                .openingDate(this.openingDate)
                .isFranchise(this.isFranchise())
                .build();
    }
}