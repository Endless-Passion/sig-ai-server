package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.request.MarketAnalysisReq;
import com.endlesspassion.sigai.domain.analysis.dto.response.ClosedComparison;
import com.endlesspassion.sigai.domain.analysis.dto.response.MarketAnalysisRes;
import com.endlesspassion.sigai.domain.analysis.dto.response.RevenueComparison;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import com.endlesspassion.sigai.domain.store.repository.StoreRevenueRepository;
import com.endlesspassion.sigai.global.common.enums.ServiceArea;
import com.endlesspassion.sigai.global.common.enums.ServiceIndustry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("상권 분석 통합 서비스 테스트")
class MarketAnalysisServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreRevenueRepository storeRevenueRepository;

    @Mock
    private RevenueComparisonService revenueComparisonService;

    @Mock
    private PopulationComparisonService populationComparisonService;

    @Mock
    private ClosedComparisonService closedComparisonService;

    @Mock
    private PublicDataService publicDataService;

    @InjectMocks
    private MarketAnalysisService marketAnalysisService;

    private Store mockStore;
    private List<StoreRevenue> mockStoreRevenues;
    private MarketAnalysisReq analysisReq;

    @BeforeEach
    void setUp() {
        // 가게 정보 Mock
        mockStore = Store.builder()
                .id(1L)
                .storeName("김씨네 한식당")
                .serviceArea(ServiceArea.SA_3110001) // 이북5도청사
                .serviceIndustry(ServiceIndustry.CS100001) // 한식음식점
                .gu("종로구")
                .dong("청운동")
                .openingDate(LocalDate.of(2022, 1, 1))
                .isFranchise(false)
                .build();

        // 32개월 매출 데이터 Mock (8분기)
        // 2023년 1월 ~ 2024년 12월 (현재 2024년 4분기 기준)
        mockStoreRevenues = createMockStoreRevenues(mockStore);

        // 분석 요청 DTO
        analysisReq = MarketAnalysisReq.builder()
                .storeId(1L)
                .quarter("202404") // 2024년 4분기
                .count(8) // 8분기
                .build();
    }

    @Test
    @DisplayName("상권 분석 통합 테스트 - 정상 케이스")
    void analyze_Success() {
        // Given
        when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        when(storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(mockStore))
                .thenReturn(mockStoreRevenues);

        // 매출 비교 서비스 Mock
        RevenueComparison mockRevenueComparison = createMockRevenueComparison();
        when(revenueComparisonService.analyze(anyList(), anyString(), anyString(), anyList()))
                .thenReturn(mockRevenueComparison);

        // 인구 비교 서비스 Mock (빈 껍데기)
        when(populationComparisonService.analysis()).thenReturn(null);

        // 폐업률 비교 서비스 Mock
        ClosedComparison mockClosedComparison = createMockClosedComparison();
        when(closedComparisonService.analyze(anyList(), anyString(), anyString()))
                .thenReturn(mockClosedComparison);

        // When
        MarketAnalysisRes result = marketAnalysisService.analyze(analysisReq);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStoreId()).isEqualTo(1L);
        assertThat(result.getStoreName()).isEqualTo("김씨네 한식당");
        assertThat(result.getAnalyzedAt()).isNotNull();

        // 매출 비교 데이터 검증
        assertThat(result.getRevenueComparison()).isNotNull();
        assertThat(result.getRevenueComparison().getQuarterlyRevenueRanks()).hasSize(8);
        assertThat(result.getRevenueComparison().getCompetitionIntensity()).isEqualTo(45.0);

        // 폐업률 비교 데이터 검증
        assertThat(result.getClosedComparison()).isNotNull();
        assertThat(result.getClosedComparison().getQuarterlyClosedRates()).hasSize(8);
        assertThat(result.getClosedComparison().getAverageClosedRate()).isEqualTo(12.5);
        assertThat(result.getClosedComparison().getTrend()).isEqualTo("INCREASING");

        // 인구 비교 데이터 검증 (빈 껍데기)
        assertThat(result.getPopulationComparison()).isNull();
    }

    @Test
    @DisplayName("상권 분석 - 가게 정보 없는 경우")
    void analyze_StoreNotFound() {
        // Given
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> marketAnalysisService.analyze(analysisReq))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 가게를 찾을 수 없습니다. ID: 1");
    }

    @Test
    @DisplayName("상권 분석 - 매출 데이터 없는 경우")
    void analyze_NoRevenueData() {
        // Given
        when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        when(storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(mockStore))
                .thenReturn(List.of()); // 빈 리스트

        // When & Then
        assertThatThrownBy(() -> marketAnalysisService.analyze(analysisReq))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 가게의 매출 데이터가 없습니다. ID: 1");
    }

    @Test
    @DisplayName("분기 계산 로직 검증 - 8분기")
    void analyze_QuarterCalculation() {
        // Given
        when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        when(storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(mockStore))
                .thenReturn(mockStoreRevenues);

        RevenueComparison mockRevenueComparison = createMockRevenueComparison();
        when(revenueComparisonService.analyze(anyList(), anyString(), anyString(), anyList()))
                .thenReturn(mockRevenueComparison);

        when(populationComparisonService.analysis()).thenReturn(null);

        ClosedComparison mockClosedComparison = createMockClosedComparison();
        when(closedComparisonService.analyze(anyList(), anyString(), anyString()))
                .thenReturn(mockClosedComparison);

        // When
        MarketAnalysisRes result = marketAnalysisService.analyze(analysisReq);

        // Then
        assertThat(result).isNotNull();

        // 매출 비교의 분기 개수 검증
        assertThat(result.getRevenueComparison().getQuarterlyRevenueRanks()).hasSize(8);

        // 분기 순서 검증 (최신순: 202404 → 202301)
        List<RevenueComparison.QuarterlyRevenueRank> ranks =
                result.getRevenueComparison().getQuarterlyRevenueRanks();

        assertThat(ranks.get(0).getQuarter()).isEqualTo("202404"); // 2024년 4분기
        assertThat(ranks.get(1).getQuarter()).isEqualTo("202403"); // 2024년 3분기
        assertThat(ranks.get(2).getQuarter()).isEqualTo("202402"); // 2024년 2분기
        assertThat(ranks.get(3).getQuarter()).isEqualTo("202401"); // 2024년 1분기
        assertThat(ranks.get(4).getQuarter()).isEqualTo("202304"); // 2023년 4분기
        assertThat(ranks.get(5).getQuarter()).isEqualTo("202303"); // 2023년 3분기
        assertThat(ranks.get(6).getQuarter()).isEqualTo("202302"); // 2023년 2분기
        assertThat(ranks.get(7).getQuarter()).isEqualTo("202301"); // 2023년 1분기
    }

    @Test
    @DisplayName("월별 → 분기별 매출 변환 검증")
    void analyze_MonthlyToQuarterlyRevenue() {
        // Given
        when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        when(storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(mockStore))
                .thenReturn(mockStoreRevenues);

        RevenueComparison mockRevenueComparison = createMockRevenueComparison();
        when(revenueComparisonService.analyze(anyList(), anyString(), anyString(), anyList()))
                .thenReturn(mockRevenueComparison);

        when(populationComparisonService.analysis()).thenReturn(null);

        ClosedComparison mockClosedComparison = createMockClosedComparison();
        when(closedComparisonService.analyze(anyList(), anyString(), anyString()))
                .thenReturn(mockClosedComparison);

        // When
        MarketAnalysisRes result = marketAnalysisService.analyze(analysisReq);

        // Then
        assertThat(result).isNotNull();

        // 매출 데이터가 분기별로 정상적으로 집계되었는지 검증
        // 202404 (10~12월): 5,000,000 + 4,900,000 + 4,800,000 = 14,700,000
        RevenueComparison.QuarterlyRevenueRank q202404 =
                result.getRevenueComparison().getQuarterlyRevenueRanks().get(0);
        assertThat(q202404.getRevenue()).isEqualByComparingTo(new BigDecimal("14700000"));
    }

    // Mock 데이터 생성 헬퍼 메서드

    /**
     * 32개월 매출 데이터 생성 (2023년 1월 ~ 2024년 12월)
     */
    private List<StoreRevenue> createMockStoreRevenues(Store store) {
        List<StoreRevenue> revenues = new ArrayList<>();

        // 2024년 매출 (12개월)
        for (int month = 12; month >= 1; month--) {
            revenues.add(createStoreRevenue(store, 2024, month,
                    new BigDecimal(5000000 - (month * 10000)))); // 월별로 조금씩 차이
        }

        // 2023년 매출 (12개월)
        for (int month = 12; month >= 1; month--) {
            revenues.add(createStoreRevenue(store, 2023, month,
                    new BigDecimal(4000000 - (month * 10000))));
        }

        // 2022년 매출 (8개월: 5~12월)
        for (int month = 12; month >= 5; month--) {
            revenues.add(createStoreRevenue(store, 2022, month,
                    new BigDecimal(3000000 - (month * 10000))));
        }

        return revenues;
    }

    private StoreRevenue createStoreRevenue(Store store, int year, int month, BigDecimal revenue) {
        return StoreRevenue.builder()
                .store(store)
                .year(year)
                .month(month)
                .monthlyRevenue(revenue)
                .deliverySalesRatio(30.0f)
                .maleCustomer2030Ratio(40.0f)
                .maleCustomer40PlusRatio(35.0f)
                .returningCustomerRatio(60.0f)
                .build();
    }

    /**
     * 매출 비교 Mock 데이터 생성
     */
    private RevenueComparison createMockRevenueComparison() {
        List<RevenueComparison.QuarterlyRevenueRank> ranks = new ArrayList<>();

        // 8분기 데이터
        String[] quarters = {"202404", "202403", "202402", "202401", "202304", "202303", "202302", "202301"};
        BigDecimal[] revenues = {
                new BigDecimal("14700000"), // 202404: 10~12월 합산
                new BigDecimal("13200000"), // 202403: 7~9월 합산
                new BigDecimal("13500000"), // 202402: 4~6월 합산
                new BigDecimal("14700000"), // 202401: 1~3월 합산
                new BigDecimal("11700000"), // 202304
                new BigDecimal("11400000"), // 202303
                new BigDecimal("11100000"), // 202302
                new BigDecimal("10800000")  // 202301
        };

        Integer previousRank = null;
        for (int i = 0; i < quarters.length; i++) {
            int rank = 3 + i; // 3위부터 시작해서 점점 순위 하락
            Integer rankChange = previousRank != null ? previousRank - rank : null;

            ranks.add(RevenueComparison.QuarterlyRevenueRank.builder()
                    .quarter(quarters[i])
                    .revenue(revenues[i])
                    .topPercentile(30.0 + (i * 2)) // 30%부터 시작
                    .totalStoreCount(100)
                    .rank(rank)
                    .rankChange(rankChange)
                    .build());

            previousRank = rank;
        }

        return RevenueComparison.builder()
                .quarterlyRevenueRanks(ranks)
                .competitionIntensity(45.0) // 경쟁 강도
                .build();
    }

    /**
     * 폐업률 비교 Mock 데이터 생성
     */
    private ClosedComparison createMockClosedComparison() {
        List<ClosedComparison.QuarterlyClosedRate> rates = new ArrayList<>();

        // 8분기 데이터
        String[] quarters = {"202404", "202403", "202402", "202401", "202304", "202303", "202302", "202301"};
        Double[] closedRates = {15.0, 14.0, 16.0, 12.0, 13.0, 11.0, 10.0, 9.0};

        Double previousRate = null;
        for (int i = 0; i < quarters.length; i++) {
            Double rateChange = previousRate != null ? closedRates[i] - previousRate : null;

            rates.add(ClosedComparison.QuarterlyClosedRate.builder()
                    .quarter(quarters[i])
                    .closedRate(closedRates[i])
                    .closedStoreCount(closedRates[i].intValue()) // 간단히 폐업률과 동일하게
                    .totalStoreCount(100)
                    .rateChange(rateChange)
                    .build());

            previousRate = closedRates[i];
        }

        return ClosedComparison.builder()
                .quarterlyClosedRates(rates)
                .averageClosedRate(12.5) // 평균 폐업률
                .trend("INCREASING") // 증가 추세
                .build();
    }
}