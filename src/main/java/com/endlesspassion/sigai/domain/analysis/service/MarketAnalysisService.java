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
    private final RankingCalculationService rankingCalculationService;
    private final CompetitionAnalysisService competitionAnalysisService;

}
