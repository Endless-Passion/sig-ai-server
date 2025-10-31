package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.response.ClosedComparison;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 폐업률 비교 분석 서비스
 *
 * 책임:
 * 1. 동일 상권, 동일 업종의 폐업률 추세 계산
 * 2. 평균 폐업률 산출
 * 3. 폐업률 증가/감소 추세 판단
 *
 * 폐업률의 의미:
 * - 높은 폐업률 = 시장 진입 장벽은 낮지만 생존이 어려움
 * - 낮은 폐업률 = 안정적이지만 진입이 어려울 수 있음
 * - 추세 분석으로 시장의 변화 방향 파악
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClosedComparisonService {

    private final PublicDataService publicDataService;

    /**
     * 폐업률 비교 분석 실행
     *
     * @param quarters 분석할 분기 리스트 (최근 분기부터 과거 순)
     * @param trdarCd 상권 코드
     * @param svcIndutyCd 업종 코드
     * @return 폐업률 비교 분석 결과
     */
    public ClosedComparison analyze(
            List<String> quarters,
            String trdarCd,
            String svcIndutyCd) {

        List<ClosedComparison.QuarterlyClosedRate> quarterlyClosedRates = new ArrayList<>();
        Double previousRate = null;
        double sumOfRates = 0.0;
        int validCount = 0;

        // 각 분기별 폐업률 계산
        for (String quarter : quarters) {
            ClosedComparison.QuarterlyClosedRate rate = calculateClosedRateForQuarter(
                    quarter, trdarCd, svcIndutyCd, previousRate);

            quarterlyClosedRates.add(rate);

            if (rate.getClosedRate() != null) {
                sumOfRates += rate.getClosedRate();
                validCount++;
                previousRate = rate.getClosedRate();
            }
        }

        // 평균 폐업률 계산
        Double averageClosedRate = validCount > 0
                ? Math.round((sumOfRates / validCount) * 10) / 10.0
                : null;

        // 폐업률 추세 판단
        String trend = determineTrend(quarterlyClosedRates);

        return ClosedComparison.builder()
                .quarterlyClosedRates(quarterlyClosedRates)
                .averageClosedRate(averageClosedRate)
                .trend(trend)
                .build();
    }

    /**
     * 특정 분기의 폐업률 계산
     *
     * 폐업률 = (폐업 점포 수 / 전체 점포 수) * 100
     *
     * 공공 데이터에서 이미 계산된 폐업률(clsbizRt)을 사용하되,
     * 없는 경우 직접 계산
     */
    private ClosedComparison.QuarterlyClosedRate calculateClosedRateForQuarter(
            String quarter,
            String trdarCd,
            String svcIndutyCd,
            Double previousRate) {

        PublicStoreData storeData = publicDataService
                .findStoreData(quarter, trdarCd, svcIndutyCd);

        if (storeData == null) {
            log.warn("분기 {}에 대한 점포 데이터가 없습니다. 상권: {}, 업종: {}",
                    quarter, trdarCd, svcIndutyCd);
            return ClosedComparison.QuarterlyClosedRate.empty(quarter);
        }

        // 폐업률 계산
        Double closedRate = null;
        Integer closedStoreCount = null;
        Integer totalStoreCount = null;

        // 공공 데이터에서 제공하는 폐업률 사용
        if (storeData.getClsbizRt() != null) {
            closedRate = storeData.getClsbizRt();
            closedRate = Math.round(closedRate * 10) / 10.0; // 소수점 첫째자리
        }
        // 없으면 직접 계산
        else if (storeData.getClsbizStorCo() != null && storeData.getStorCo() != null) {
            closedStoreCount = storeData.getClsbizStorCo().intValue();
            totalStoreCount = storeData.getStorCo().intValue();

            if (totalStoreCount > 0) {
                closedRate = ((double) closedStoreCount / totalStoreCount) * 100;
                closedRate = Math.round(closedRate * 10) / 10.0;
            }
        }

        // 점포 수 정보
        if (storeData.getClsbizStorCo() != null) {
            closedStoreCount = storeData.getClsbizStorCo().intValue();
        }
        if (storeData.getStorCo() != null) {
            totalStoreCount = storeData.getStorCo().intValue();
        }

        // 전 분기 대비 변화율
        Double rateChange = null;
        if (previousRate != null && closedRate != null) {
            rateChange = closedRate - previousRate;
            rateChange = Math.round(rateChange * 10) / 10.0;
        }

        return ClosedComparison.QuarterlyClosedRate.builder()
                .quarter(quarter)
                .closedRate(closedRate)
                .closedStoreCount(closedStoreCount)
                .totalStoreCount(totalStoreCount)
                .rateChange(rateChange)
                .build();
    }

    /**
     * 폐업률 추세 판단
     *
     * 로직:
     * 1. 최근 3개 분기의 평균과 과거 3개 분기의 평균 비교
     * 2. 차이가 1% 이상이면 증가/감소, 그 외는 안정
     *
     * 왜 이 방식인가?
     * - 단기 변동성을 완화하고 전반적인 추세 파악
     * - 1% 임계값은 통계적 유의미성 고려 (조정 가능)
     */
    private String determineTrend(List<ClosedComparison.QuarterlyClosedRate> rates) {
        if (rates.size() < 6) {
            return "STABLE"; // 데이터가 부족하면 안정으로 판단
        }

        // 최근 3개 분기 평균
        double recentAvg = rates.subList(0, 3).stream()
                .filter(r -> r.getClosedRate() != null)
                .mapToDouble(ClosedComparison.QuarterlyClosedRate::getClosedRate)
                .average()
                .orElse(0.0);

        // 과거 3개 분기 평균
        double pastAvg = rates.subList(3, 6).stream()
                .filter(r -> r.getClosedRate() != null)
                .mapToDouble(ClosedComparison.QuarterlyClosedRate::getClosedRate)
                .average()
                .orElse(0.0);

        double difference = recentAvg - pastAvg;

        if (difference > 1.0) {
            return "INCREASING"; // 폐업률 증가 추세
        } else if (difference < -1.0) {
            return "DECREASING"; // 폐업률 감소 추세
        } else {
            return "STABLE"; // 안정적
        }
    }
}