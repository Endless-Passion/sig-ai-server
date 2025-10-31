package com.endlesspassion.sigai.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // private final StoreService storeService;
    private final RevenueComparisonService revenueComparisonService;
    private final PopulationComparisonService populationComparisonService; // 이건 구현 안할 예정. 껍데기만 남기고, DTO도 빈 껍데기로만 제공 예정!, 프론트에서 알아서 가짜 데이터로 처리
    private final ClosedComparisonService closedComparisonService;

    // 공공 데이터로부터 다음 정보 가져오기
    // 매출 비교 그래프(8분기 동안 우리 가게의 매출은 동일 상권, 동일 업종의 상위 몇%인지 순위 추세)
    // 인구(8분기 동안 유동/거주/직장 인구)의 실시간 데이터 변화 추세(이건 시간상 못함 나중에 할 예정!, 지금 구현안하고 껍데기만)
    // 8분기 동안 동일 업종, 동일 상권의 폐업률 데이터 추세 데이터
    //경쟁강도 = 동일업종 점포수 / 상권 면적(면적 미존재 시 점포수 지표만)
    private void analysis() {
        // 아래 서비스에서 데이터를 불러와서 맞게 DTO로 구성!
        revenueComparisonService.alalysis();
        populationComparisonService.alalysis(); // 위에서 말했듯이 이건 안함!
        closedComparisonService.alalysis();
    }

}
