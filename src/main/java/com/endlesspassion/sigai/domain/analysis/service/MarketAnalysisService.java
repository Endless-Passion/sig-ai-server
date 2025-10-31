package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.request.MarketAnalysisReq;
import com.endlesspassion.sigai.domain.analysis.dto.response.MarketAnalysisRes;
import com.endlesspassion.sigai.domain.store.StoreService;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
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
        BigDecimal revenue = null; // 사장님 가게로부터 이번 count 분기만큼 분기별 수익 가져오기!

        List<String> quarters = getQuarters(req.getQuarter(), req.getCount());

        return MarketAnalysisRes.of(
                req.getStoreId(),
                store.getStoreName(),
                revenueComparisonService.alalyze(quarters, trdarCd, svcIndutyCd, revenue),
                populationComparisonService.alalysis(),
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
}
