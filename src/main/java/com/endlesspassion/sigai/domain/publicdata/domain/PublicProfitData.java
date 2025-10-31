package com.endlesspassion.sigai.domain.publicdata.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "public_data")
@Data
@CompoundIndex(name = "idx_unique_sales_data",
        def = "{'stdr_yyqu_cd': 1, 'trdar_cd': 1, 'svc_induty_cd': 1}",
        unique = true)
public class PublicProfitData {

    @Id
    private String id;

    // --- 상권 및 업종 정보 (String) ---
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

    // --- 매출 금액 (Double) ---
    @Field("thsmon_selng_amt")
    private Double thsmonSelngAmt; // 당월_매출_금액

    @Field("ml_selng_amt")
    private Double mlSelngAmt; // 남성_매출_금액

    @Field("fml_selng_amt")
    private Double fmlSelngAmt; // 여성_매출_금액

    // --- 매출 건수 (Double) ---
    @Field("thsmon_selng_co")
    private Double thsmonSelngCo; // 당월_매출_건수

    @Field("mdwk_selng_co")
    private Double mdwkSelngCo; // 주중_매출_건수

    @Field("ml_selng_co")
    private Double mlSelngCo; // 남성_매출_건수

    @Field("fml_selng_co")
    private Double fmlSelngCo; // 여성_매출_건수

    @Field("agrde_10_selng_co")
    private Double agrde10SelngCo; // 연령대_10_매출_건수

    @Field("agrde_20_selng_co")
    private Double agrde20SelngCo; // 연령대_20_매출_건수

    @Field("agrde_30_selng_co")
    private Double agrde30SelngCo; // 연령대_30_매출_건수

    @Field("agrde_40_selng_co")
    private Double agrde40SelngCo; // 연령대_40_매출_건수

    @Field("agrde_50_selng_co")
    private Double agrde50SelngCo; // 연령대_50_매출_건수

    @Field("agrde_60_above_selng_co")
    private Double agrde60AboveSelngCo; // 연령대_60_이상_매출_건수
}