package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.request.MarketAnalysisReq;
import com.endlesspassion.sigai.domain.analysis.dto.response.MarketAnalysisRes;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import com.endlesspassion.sigai.domain.store.repository.StoreRevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 상권 분석 통합 서비스 (Facade)
 *
 * 역할: 여러 분석 서비스를 조율하고 결과를 통합
 *
 * 왜 Facade 패턴?
 * - 클라이언트는 하나의 메서드만 호출하면 됨
 * - 내부적으로 복잡한 서비스 호출을 숨김
 * - 각 분석 서비스는 독립적으로 개발/테스트 가능
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MarketAnalysisService {

    private final StoreRepository storeRepository;
    private final StoreRevenueRepository storeRevenueRepository;
    private final RevenueComparisonService revenueComparisonService;
    private final PopulationComparisonService populationComparisonService; // 이건 구현 안할 예정. 껍데기만 남기고, DTO도 빈 껍데기로만 제공 예정!, 프론트에서 알아서 가짜 데이터로 처리
    private final ClosedComparisonService closedComparisonService;

    /**
     * Req에서 받는 값들: 가게 정보(매출, 상권, 업종, 분석할 분기)
     * 사장님 정보를 상권 정보와 비교하여 매출, 인구, 폐업 데이터 분석하기
     * revenueComparisonService: 공공 데이터의 해당 분기들의 매출 통계 정보
     * populationComparisonService: 공공 데이터의 해당 분기들의 인구 통계 정보(나중에 할 예정)
     * closedComparisonService: 공공 데이터의 해당 분기들의 폐업 통계 정보
     * 경쟁강도 = 동일업종 점포수 / 상권 면적(면적 미존재 시 점포수 지표만)(시간 되면)
     *
     * @return 사장님 가게 정보와 동일 상권/동일 업종의 공공 데이터를 비교한 결과
     */
    public MarketAnalysisRes analyze(MarketAnalysisReq req) {

        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + req.getStoreId()));

        String trdarCd = store.getServiceArea().getCode(); // 상권 이름 -> 상권 코드
        String svcIndutyCd = store.getServiceIndustry().getCode(); // 업종 이름 -> 업종 코드
        List<String> quarters = getQuarters(req.getQuarter(), req.getCount());

        // 해당 가게의 월별 매출 데이터를 조회하여 분기별로 변환
        List<StoreRevenue> monthlyRevenueList = storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(store);
        if (monthlyRevenueList.isEmpty()) {
            throw new IllegalArgumentException("해당 가게의 매출 데이터가 없습니다. ID: " + req.getStoreId());
        }

        // 월별 매출을 분기별로 그룹화하여 합산
        List<BigDecimal> quarterlyRevenue = convertMonthlyToQuarterly(monthlyRevenueList, quarters);

        return MarketAnalysisRes.of(
                req.getStoreId(),
                store.getStoreName(),
                revenueComparisonService.analyze(quarters, trdarCd, svcIndutyCd, quarterlyRevenue),
                populationComparisonService.analysis(),
                closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd)
        );
    }

    // Utils
    /**
     * 현재 분기로부터 과거 분기들을 계산하여 리스트로 반환
     *
     * @param quarter 시작 분기 (YYYYQQ 형식, 예: 202401)
     * @param count 반환할 분기 개수
     * @return 현재 분기를 포함한 과거 분기들의 리스트 (최신 순서)
     *
     * 예시:
     * - 입력: quarter = "202401", count = 8
     * - 출력: ["202401", "202304", "202303", "202302", "202301", "202204", "202203", "202202"]
     */
    private List<String> getQuarters(String quarter, int count) {
        java.util.List<String> quarters = new java.util.ArrayList<>();

        // 년도와 분기 추출
        int year = Integer.parseInt(quarter.substring(0, 4));
        int quarterNum = Integer.parseInt(quarter.substring(4, 6));

        // count만큼 분기를 역순으로 계산
        for (int i = 0; i < count; i++) {
            // 현재 분기를 리스트에 추가 (YYYYQQ 형식)
            quarters.add(String.format("%04d%02d", year, quarterNum));

            // 이전 분기로 이동
            quarterNum--;
            if (quarterNum < 1) {
                quarterNum = 4;
                year--;
            }
        }

        return quarters;
    }

    /**
     * 월별 매출 데이터를 분기별로 그룹화하여 합산
     *
     * @param monthlyRevenueList 월별 매출 데이터 리스트
     * @param quarters 필요한 분기 리스트 (YYYYQQ 형식, 최신순)
     * @return 분기별 매출 리스트 (quarters 순서와 동일)
     *
     * 예시:
     * - quarters = ["202404", "202403", "202402", "202401"]
     * - 202404: 2024년 10, 11, 12월 매출 합산
     * - 202403: 2024년 7, 8, 9월 매출 합산
     * - 202402: 2024년 4, 5, 6월 매출 합산
     * - 202401: 2024년 1, 2, 3월 매출 합산
     */
    private List<BigDecimal> convertMonthlyToQuarterly(List<StoreRevenue> monthlyRevenueList, List<String> quarters) {
        // 분기별 매출을 저장할 맵 (key: YYYYQQ, value: 합산된 매출)
        java.util.Map<String, BigDecimal> quarterlyRevenueMap = new java.util.HashMap<>();

        // 각 분기를 0으로 초기화
        for (String quarter : quarters) {
            quarterlyRevenueMap.put(quarter, BigDecimal.ZERO);
        }

        // 월별 매출을 분기별로 그룹화하여 합산
        for (StoreRevenue revenue : monthlyRevenueList) {
            int year = revenue.getYear();
            int month = revenue.getMonth();

            // 월을 분기로 변환 (1~3월 -> 1분기, 4~6월 -> 2분기, 7~9월 -> 3분기, 10~12월 -> 4분기)
            int quarterNum = (month - 1) / 3 + 1;
            String quarterKey = String.format("%04d%02d", year, quarterNum);

            // 요청된 분기에 해당하는 경우에만 합산
            if (quarterlyRevenueMap.containsKey(quarterKey)) {
                BigDecimal currentTotal = quarterlyRevenueMap.get(quarterKey);
                quarterlyRevenueMap.put(quarterKey, currentTotal.add(revenue.getMonthlyRevenue()));
            }
        }

        // quarters 순서대로 결과 리스트 생성
        java.util.List<BigDecimal> result = new java.util.ArrayList<>();
        for (String quarter : quarters) {
            result.add(quarterlyRevenueMap.get(quarter));
        }

        return result;
    }

}
