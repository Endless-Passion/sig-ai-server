package com.endlesspassion.sigai.domain.publicdata.domain;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "public_data_store") // "점포" 데이터를 위한 별도 컬렉션
@Data
// '매출' 데이터와 동일한 키로 인덱스를 생성 (나중에 조인/매칭하기 위함)
@CompoundIndex(name = "idx_unique_store_data",
        def = "{'stdr_yyqu_cd': 1, 'trdar_cd': 1, 'svc_induty_cd': 1}",
        unique = true)
public class PublicStoreData {
    @Id
    private String id;

    // --- 1. 공통 키 필드 (7개) ---
    // (매출 데이터와 중복되지만, 조인을 위해 이 컬렉션에도 저장)

    @Field("stdr_yyqu_cd")
    private String stdrYyquCd; // 기준_년분기_코드

    @Field("trdar_se_cd")
    private String trdarSeCd; // 상권_구분_코드

    @Field("trdar_se_cd_nm")
    private String trdarSeCdNm; // 상권_구분_코드_명

    @Field("trdar_cd")
    private String trdarCd; // 상권_코드

    @Field("trdar_cd_nm")
    private String trdarCdNm; // 상권_코드_명

    @Field("svc_induty_cd")
    private String svcIndutyCd; // 서비스_업종_코드

    @Field("svc_induty_cd_nm")
    private String svcIndutyCdNm; // 서비스_업종_코드_명

    // --- 2. 점포 고유 필드 (7개) ---
    // (API 응답이 .0으로 끝나는 숫자가 많으므로 Double이 안전합니다)

    @Field("stor_co")
    private Double storCo; // 점포_수

    @Field("similr_induty_stor_co")
    private Double similrIndutyStorCo; // 유사_업종_점포_수

    @Field("opbiz_rt")
    private Double opbizRt; // 개업_율

    @Field("opbiz_stor_co")
    private Double opbizStorCo; // 개업_점포_수

    @Field("clsbiz_rt")
    private Double clsbizRt; // 폐업_률

    @Field("clsbiz_stor_co")
    private Double clsbizStorCo; // 폐업_점포_수

    @Field("frc_stor_co")
    private Double frcStorCo; // 프랜차이즈_점포_수
}
