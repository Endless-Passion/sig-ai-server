package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.response.RevenueComparison;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("매출 비교 분석 서비스 테스트")
class RevenueComparisonServiceTest {

    @Mock
    private PublicDataService publicDataService;

    @InjectMocks
    private RevenueComparisonService revenueComparisonService;

    private List<String> quarters;
    private List<BigDecimal> revenues;
    private String trdarCd;
    private String svcIndutyCd;

    @BeforeEach
    void setUp() {
        // 8분기 데이터 (최신순)
        quarters = List.of(
                "202404", "202403", "202402", "202401",
                "202304", "202303", "202302", "202301"
        );

        // 사장님 가게의 분기별 매출 (8분기)
        revenues = List.of(
                new BigDecimal("15000000"), // 202404: 1500만원 (3위)
                new BigDecimal("14000000"), // 202403: 1400만원 (3위)
                new BigDecimal("13000000"), // 202402: 1300만원 (4위)
                new BigDecimal("12000000"), // 202401: 1200만원 (5위)
                new BigDecimal("11000000"), // 202304: 1100만원 (6위)
                new BigDecimal("10000000"), // 202303: 1000만원 (7위)
                new BigDecimal("9000000"),  // 202302: 900만원 (8위)
                new BigDecimal("8000000")   // 202301: 800만원 (9위)
        );

        trdarCd = "1000001"; // 공릉동 국수거리
        svcIndutyCd = "CS100001"; // 한식
    }

    @Test
    @DisplayName("매출 순위 계산 - 정상 케이스")
    void analyze_Success() {
        // Given
        // 202404 분기의 공공 데이터 (총 10개 매장, 내림차순 정렬)
        List<PublicProfitData> quarter202404Data = createMockProfitData("202404", List.of(
                20000000.0, 18000000.0, 15000000.0, // 사장님 가게: 3위 (상위 30%)
                14000000.0, 13000000.0, 12000000.0,
                11000000.0, 10000000.0, 9000000.0, 8000000.0
        ));

        // 202403 분기의 공공 데이터
        List<PublicProfitData> quarter202403Data = createMockProfitData("202403", List.of(
                19000000.0, 17000000.0, 14000000.0, // 사장님 가게: 3위 (상위 30%)
                13000000.0, 12000000.0, 11000000.0,
                10000000.0, 9000000.0, 8000000.0, 7000000.0
        ));

        // Mock 설정
        when(publicDataService.findAllProfitDataByMarketAndIndustry("202404", trdarCd, svcIndutyCd))
                .thenReturn(quarter202404Data);
        when(publicDataService.findAllProfitDataByMarketAndIndustry("202403", trdarCd, svcIndutyCd))
                .thenReturn(quarter202403Data);

        // 나머지 분기들도 Mock 설정 (간단하게)
        for (int i = 2; i < quarters.size(); i++) {
            when(publicDataService.findAllProfitDataByMarketAndIndustry(quarters.get(i), trdarCd, svcIndutyCd))
                    .thenReturn(createMockProfitData(quarters.get(i), List.of(
                            20000000.0, 18000000.0, 16000000.0, 14000000.0, 12000000.0,
                            10000000.0, 8000000.0, 6000000.0, 4000000.0, 2000000.0
                    )));
        }

        // 경쟁 강도 Mock (최근 분기 기준)
        PublicStoreData storeData = new PublicStoreData();
        storeData.setStorCo(45.0); // 동일 업종 점포 수: 45개
        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(storeData);

        // When
        RevenueComparison result = revenueComparisonService.analyze(quarters, trdarCd, svcIndutyCd, revenues);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getQuarterlyRevenueRanks()).hasSize(8);
        assertThat(result.getCompetitionIntensity()).isEqualTo(45.0);

        // 첫 번째 분기 검증 (202404)
        RevenueComparison.QuarterlyRevenueRank firstQuarter = result.getQuarterlyRevenueRanks().get(0);
        assertThat(firstQuarter.getQuarter()).isEqualTo("202404");
        assertThat(firstQuarter.getRevenue()).isEqualByComparingTo(new BigDecimal("15000000"));
        assertThat(firstQuarter.getRank()).isEqualTo(3);
        assertThat(firstQuarter.getTotalStoreCount()).isEqualTo(10);
        assertThat(firstQuarter.getTopPercentile()).isEqualTo(30.0); // 3/10 * 100 = 30%
        assertThat(firstQuarter.getRankChange()).isNull(); // 첫 분기는 변화 없음

        // 두 번째 분기 검증 (202403)
        RevenueComparison.QuarterlyRevenueRank secondQuarter = result.getQuarterlyRevenueRanks().get(1);
        assertThat(secondQuarter.getQuarter()).isEqualTo("202403");
        assertThat(secondQuarter.getRank()).isEqualTo(3);
        assertThat(secondQuarter.getRankChange()).isEqualTo(0); // 3 - 3 = 0 (순위 유지)
    }

    @Test
    @DisplayName("매출 순위 계산 - 데이터 없는 경우")
    void analyze_NoData() {
        // Given
        when(publicDataService.findAllProfitDataByMarketAndIndustry(anyString(), anyString(), anyString()))
                .thenReturn(List.of()); // 빈 리스트

        when(publicDataService.findStoreData(anyString(), anyString(), anyString()))
                .thenReturn(null);

        // When
        RevenueComparison result = revenueComparisonService.analyze(quarters, trdarCd, svcIndutyCd, revenues);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getQuarterlyRevenueRanks()).hasSize(8);
        assertThat(result.getCompetitionIntensity()).isNull();

        // 모든 분기가 empty 상태
        result.getQuarterlyRevenueRanks().forEach(rank -> {
            assertThat(rank.getRank()).isNull();
            assertThat(rank.getTopPercentile()).isNull();
            assertThat(rank.getTotalStoreCount()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("매출 순위 계산 - 분기 수와 매출 데이터 수 불일치")
    void analyze_MismatchedSize() {
        // Given
        List<BigDecimal> wrongRevenues = List.of(
                new BigDecimal("15000000"),
                new BigDecimal("14000000")
        ); // 2개만 (8개여야 함)

        // When & Then
        assertThatThrownBy(() ->
                revenueComparisonService.analyze(quarters, trdarCd, svcIndutyCd, wrongRevenues)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("분기 수와 매출 데이터 수가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("순위 변화 계산 - 순위 상승")
    void analyze_RankImproved() {
        // Given
        // 202404: 3위
        List<PublicProfitData> quarter202404Data = createMockProfitData("202404", List.of(
                20000000.0, 18000000.0, 15000000.0, 14000000.0, 13000000.0,
                12000000.0, 11000000.0, 10000000.0, 9000000.0, 8000000.0
        ));

        // 202403: 5위 (이전 분기에서는 순위가 낮았음)
        List<PublicProfitData> quarter202403Data = createMockProfitData("202403", List.of(
                20000000.0, 18000000.0, 16000000.0, 15000000.0, 14000000.0, // 5위
                13000000.0, 12000000.0, 11000000.0, 10000000.0, 9000000.0
        ));

        when(publicDataService.findAllProfitDataByMarketAndIndustry("202404", trdarCd, svcIndutyCd))
                .thenReturn(quarter202404Data);
        when(publicDataService.findAllProfitDataByMarketAndIndustry("202403", trdarCd, svcIndutyCd))
                .thenReturn(quarter202403Data);

        // 나머지 분기 Mock
        for (int i = 2; i < quarters.size(); i++) {
            when(publicDataService.findAllProfitDataByMarketAndIndustry(quarters.get(i), trdarCd, svcIndutyCd))
                    .thenReturn(createMockProfitData(quarters.get(i), List.of(
                            20000000.0, 18000000.0, 16000000.0, 14000000.0, 12000000.0,
                            10000000.0, 8000000.0, 6000000.0, 4000000.0, 2000000.0
                    )));
        }

        PublicStoreData storeData = new PublicStoreData();
        storeData.setStorCo(45.0);
        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(storeData);

        // When
        RevenueComparison result = revenueComparisonService.analyze(quarters, trdarCd, svcIndutyCd, revenues);

        // Then
        RevenueComparison.QuarterlyRevenueRank secondQuarter = result.getQuarterlyRevenueRanks().get(1);
        assertThat(secondQuarter.getRankChange()).isEqualTo(-2); // 5위 -> 3위 = 5 - 3 = 2 (음수는 순위 하락 의미이지만, 로직상 previousRank - currentRank이므로 실제로는 순위 상승)
    }

    // Mock 데이터 생성 헬퍼 메서드
    private List<PublicProfitData> createMockProfitData(String quarter, List<Double> revenues) {
        List<PublicProfitData> dataList = new ArrayList<>();
        for (Double revenue : revenues) {
            PublicProfitData data = new PublicProfitData();
            data.setStdrYyquCd(quarter);
            data.setTrdarCd(trdarCd);
            data.setSvcIndutyCd(svcIndutyCd);
            data.setThsmonSelngAmt(revenue);
            dataList.add(data);
        }
        return dataList;
    }
}