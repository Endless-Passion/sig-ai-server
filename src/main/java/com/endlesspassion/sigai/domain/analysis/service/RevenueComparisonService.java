package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.response.RevenueComparison;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 매출 비교 분석 서비스
 *
 * 책임:
 * 1. 동일 상권, 동일 업종 내에서 사용자 매장의 매출 순위 계산
 * 2. 상위 퍼센타일 계산
 * 3. 경쟁 강도 지수 계산
 *
 * 알고리즘 설계 이유:
 * - MongoDB에서 윈도우 함수나 복잡한 집계는 제한적
 * - 애플리케이션 레벨에서 정렬 및 순위 계산이 더 명확하고 유지보수 용이
 * - 분기별 데이터량이 크지 않으므로 메모리 처리 가능
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class RevenueComparisonService {

    private final PublicDataService publicDataService;


    // 공공 데이터로부터 다음 정보 가져오기
    // 매출 비교 그래프
    //-동일 상권, 동일 업종 내 내 가게가 상위% (매출 상대지표)
    //-순위 변화
    //경쟁강도 = 동일업종 점포수 / 상권 면적(면적 미존재 시 점포수 지표만)

    // 매출 비교 분석 함수
    public RevenueComparison alalyze(
            List<String> quarters,
            String trdarCd,
            String svcIndutyCd,
            BigDecimal revenue
    ) {
        List<RevenueComparison.QuarterlyRevenueRank> quarterlyRanks = new ArrayList<>();
        Integer previousRank = null;

        // 각 분기별로 순위 계산
        for(String quarger : quarters) {
            RevenueComparison.QuarterlyRevenueRank rank = calculateRankForQuarter(
                    quarger, trdarCd, svcIndutyCd, revenue, previousRank);
            quarterlyRanks.add(rank);
            previousRank = rank.getRank();
        }

        // 경쟁 강도 계산 (최근 분기 기준)
        Double competitionIntensity = calculateCompetitionIntensity(
                quarters.get(0), trdarCd, svcIndutyCd);

        return RevenueComparison.of(quarterlyRanks, competitionIntensity);
    }

    private RevenueComparison.QuarterlyRevenueRank calculateRankForQuarter(
            String quarter,
            String trdarCd,
            String svcIndutyCd,
            BigDecimal revenue,
            Integer previousRank
    ) {
        // 1. 동일 상권, 동일 업종의 모든 매출 데이터 조회(내림차순)
        List<PublicProfitData> allData = publicDataService
                .findAllProfitDataByMarketAndIndustry(quarter, trdarCd, svcIndutyCd);
        if(allData.isEmpty()) {
            log.warn("분기 {}에 대한 데이터가 없습니다. 상권: {}, 업종: {}",
                    quarter, trdarCd, svcIndutyCd);
            return RevenueComparison.QuarterlyRevenueRank.empty(quarter, revenue);
        }

        // 2. 사장님 순위 계산(사장님보다 매출이 높은 매장수 + 1)
        int rank = 1;
        for (PublicProfitData data : allData) {
            if (data.getThsmonSelngAmt() != null &&
                    BigDecimal.valueOf(data.getThsmonSelngAmt()).compareTo(revenue) > 0) {
                rank++;
            } else {
                break; // 이미 내림차순 정렬되어 있으므로 더 이상 확인 불필요
            }
        }

        int totalStores = allData.size();

        // 3. 상위 퍼센타일 계산
        double topPercentile = ((double)rank / totalStores) * 100;
        topPercentile = Math.round(topPercentile * 10.0) / 10.0; // 소수점 첫째 자리)

        // 4. 전 분기 대비 순위 변화
        Integer rankChange = null;
        if(previousRank != null) {
            rankChange = previousRank - rank;
        }

        return RevenueComparison.QuarterlyRevenueRank.of(quarter, revenue, topPercentile, totalStores, rank, rankChange);
    }

    private Double calculateCompetitionIntensity(
            String quarter,
            String trdarCd,
            String svcIndutyCd) {

        PublicStoreData storeData = publicDataService
                .findStoreData(quarter, trdarCd, svcIndutyCd);

        if (storeData == null || storeData.getStorCo() == null) {
            log.warn("경쟁 강도 계산 실패: 점포 데이터 없음. 분기: {}, 상권: {}, 업종: {}",
                    quarter, trdarCd, svcIndutyCd);
            return null;
        }

        return storeData.getStorCo();
    }
}
