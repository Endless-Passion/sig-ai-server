package com.endlesspassion.sigai.domain.analysis.service;

import com.endlesspassion.sigai.domain.analysis.dto.response.ClosedComparison;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("폐업률 비교 분석 서비스 테스트")
class ClosedComparisonServiceTest {

    @Mock
    private PublicDataService publicDataService;

    @InjectMocks
    private ClosedComparisonService closedComparisonService;

    private List<String> quarters;
    private String trdarCd;
    private String svcIndutyCd;

    @BeforeEach
    void setUp() {
        // 8분기 데이터 (최신순)
        quarters = List.of(
                "202404", "202403", "202402", "202401",
                "202304", "202303", "202302", "202301"
        );

        trdarCd = "1000001"; // 공릉동 국수거리
        svcIndutyCd = "CS100001"; // 한식
    }

    @Test
    @DisplayName("폐업률 분석 - 정상 케이스 (증가 추세)")
    void analyze_IncreasingTrend() {
        // Given
        // 최근 3개 분기: 평균 15% (15 + 14 + 16) / 3 = 15%
        // 과거 3개 분기: 평균 12% (12 + 13 + 11) / 3 = 12%
        // 차이: 15 - 12 = 3% > 1% → INCREASING

        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202404", 15.0, 15, 100));

        when(publicDataService.findStoreData("202403", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202403", 14.0, 14, 100));

        when(publicDataService.findStoreData("202402", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202402", 16.0, 16, 100));

        when(publicDataService.findStoreData("202401", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202401", 12.0, 12, 100));

        when(publicDataService.findStoreData("202304", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202304", 13.0, 13, 100));

        when(publicDataService.findStoreData("202303", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202303", 11.0, 11, 100));

        when(publicDataService.findStoreData("202302", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202302", 10.0, 10, 100));

        when(publicDataService.findStoreData("202301", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202301", 9.0, 9, 100));

        // When
        ClosedComparison result = closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getQuarterlyClosedRates()).hasSize(8);
        assertThat(result.getTrend()).isEqualTo("INCREASING"); // 폐업률 증가 추세
        assertThat(result.getAverageClosedRate()).isEqualTo(12.5); // (15+14+16+12+13+11+10+9)/8 = 100/8 = 12.5

        // 첫 번째 분기 검증 (202404)
        ClosedComparison.QuarterlyClosedRate firstQuarter = result.getQuarterlyClosedRates().get(0);
        assertThat(firstQuarter.getQuarter()).isEqualTo("202404");
        assertThat(firstQuarter.getClosedRate()).isEqualTo(15.0);
        assertThat(firstQuarter.getClosedStoreCount()).isEqualTo(15);
        assertThat(firstQuarter.getTotalStoreCount()).isEqualTo(100);
        assertThat(firstQuarter.getRateChange()).isNull(); // 첫 분기는 변화 없음

        // 두 번째 분기 검증 (202403)
        ClosedComparison.QuarterlyClosedRate secondQuarter = result.getQuarterlyClosedRates().get(1);
        assertThat(secondQuarter.getQuarter()).isEqualTo("202403");
        assertThat(secondQuarter.getClosedRate()).isEqualTo(14.0);
        assertThat(secondQuarter.getRateChange()).isEqualTo(-1.0); // 14.0 - 15.0 = -1.0 (감소)
    }

    @Test
    @DisplayName("폐업률 분석 - 감소 추세")
    void analyze_DecreasingTrend() {
        // Given
        // 최근 3개 분기: 평균 8% (8 + 9 + 7) / 3 = 8%
        // 과거 3개 분기: 평균 12% (12 + 11 + 13) / 3 = 12%
        // 차이: 8 - 12 = -4% < -1% → DECREASING

        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202404", 8.0, 8, 100));

        when(publicDataService.findStoreData("202403", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202403", 9.0, 9, 100));

        when(publicDataService.findStoreData("202402", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202402", 7.0, 7, 100));

        when(publicDataService.findStoreData("202401", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202401", 12.0, 12, 100));

        when(publicDataService.findStoreData("202304", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202304", 11.0, 11, 100));

        when(publicDataService.findStoreData("202303", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202303", 13.0, 13, 100));

        when(publicDataService.findStoreData("202302", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202302", 10.0, 10, 100));

        when(publicDataService.findStoreData("202301", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202301", 9.0, 9, 100));

        // When
        ClosedComparison result = closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTrend()).isEqualTo("DECREASING"); // 폐업률 감소 추세
        assertThat(result.getAverageClosedRate()).isEqualTo(9.9); // (8+9+7+12+11+13+10+9)/8 = 79/8 = 9.875 → 반올림 9.9
    }

    @Test
    @DisplayName("폐업률 분석 - 안정 추세")
    void analyze_StableTrend() {
        // Given
        // 최근 3개 분기: 평균 10.3% (10 + 11 + 10) / 3 ≈ 10.3%
        // 과거 3개 분기: 평균 10.0% (10 + 9 + 11) / 3 ≈ 10.0%
        // 차이: 0.3% → -1% < 차이 < 1% → STABLE

        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202404", 10.0, 10, 100));

        when(publicDataService.findStoreData("202403", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202403", 11.0, 11, 100));

        when(publicDataService.findStoreData("202402", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202402", 10.0, 10, 100));

        when(publicDataService.findStoreData("202401", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202401", 10.0, 10, 100));

        when(publicDataService.findStoreData("202304", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202304", 9.0, 9, 100));

        when(publicDataService.findStoreData("202303", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202303", 11.0, 11, 100));

        when(publicDataService.findStoreData("202302", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202302", 10.0, 10, 100));

        when(publicDataService.findStoreData("202301", trdarCd, svcIndutyCd))
                .thenReturn(createMockStoreData("202301", 10.0, 10, 100));

        // When
        ClosedComparison result = closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTrend()).isEqualTo("STABLE"); // 안정 추세
        assertThat(result.getAverageClosedRate()).isEqualTo(10.1); // (10+11+10+10+9+11+10+10)/8 = 81/8 = 10.125 → 반올림 10.1
    }

    @Test
    @DisplayName("폐업률 분석 - 데이터 없는 경우")
    void analyze_NoData() {
        // Given
        when(publicDataService.findStoreData(anyString(), anyString(), anyString()))
                .thenReturn(null);

        // When
        ClosedComparison result = closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getQuarterlyClosedRates()).hasSize(8);
        assertThat(result.getAverageClosedRate()).isNull();
        assertThat(result.getTrend()).isEqualTo("STABLE"); // 데이터 없으면 STABLE

        // 모든 분기가 empty 상태
        result.getQuarterlyClosedRates().forEach(rate -> {
            assertThat(rate.getClosedRate()).isNull();
            assertThat(rate.getClosedStoreCount()).isNull();
            assertThat(rate.getTotalStoreCount()).isNull();
        });
    }

    @Test
    @DisplayName("폐업률 분석 - 폐업률이 공공 데이터에 없어서 직접 계산")
    void analyze_CalculateClosedRateManually() {
        // Given
        PublicStoreData storeData = new PublicStoreData();
        storeData.setStdrYyquCd("202404");
        storeData.setTrdarCd(trdarCd);
        storeData.setSvcIndutyCd(svcIndutyCd);
        storeData.setClsbizRt(null); // 폐업률 제공 안함
        storeData.setClsbizStorCo(15.0); // 폐업 점포 수: 15개
        storeData.setStorCo(100.0); // 전체 점포 수: 100개

        when(publicDataService.findStoreData("202404", trdarCd, svcIndutyCd))
                .thenReturn(storeData);

        // 나머지는 정상 데이터
        for (int i = 1; i < quarters.size(); i++) {
            when(publicDataService.findStoreData(quarters.get(i), trdarCd, svcIndutyCd))
                    .thenReturn(createMockStoreData(quarters.get(i), 10.0, 10, 100));
        }

        // When
        ClosedComparison result = closedComparisonService.analyze(quarters, trdarCd, svcIndutyCd);

        // Then
        ClosedComparison.QuarterlyClosedRate firstQuarter = result.getQuarterlyClosedRates().get(0);
        assertThat(firstQuarter.getClosedRate()).isEqualTo(15.0); // 15/100 * 100 = 15.0%
        assertThat(firstQuarter.getClosedStoreCount()).isEqualTo(15);
        assertThat(firstQuarter.getTotalStoreCount()).isEqualTo(100);
    }

    @Test
    @DisplayName("폐업률 분석 - 분기가 6개 미만인 경우 추세는 STABLE")
    void analyze_LessThan6Quarters_StableTrend() {
        // Given
        List<String> fewQuarters = List.of("202404", "202403", "202402", "202401", "202304");

        for (String quarter : fewQuarters) {
            when(publicDataService.findStoreData(quarter, trdarCd, svcIndutyCd))
                    .thenReturn(createMockStoreData(quarter, 10.0, 10, 100));
        }

        // When
        ClosedComparison result = closedComparisonService.analyze(fewQuarters, trdarCd, svcIndutyCd);

        // Then
        assertThat(result.getTrend()).isEqualTo("STABLE"); // 6개 미만이면 무조건 STABLE
    }

    // Mock 데이터 생성 헬퍼 메서드
    private PublicStoreData createMockStoreData(String quarter, Double closedRate,
                                                 Integer closedStoreCount, Integer totalStoreCount) {
        PublicStoreData storeData = new PublicStoreData();
        storeData.setStdrYyquCd(quarter);
        storeData.setTrdarCd(trdarCd);
        storeData.setSvcIndutyCd(svcIndutyCd);
        storeData.setClsbizRt(closedRate);
        storeData.setClsbizStorCo(closedStoreCount.doubleValue());
        storeData.setStorCo(totalStoreCount.doubleValue());
        return storeData;
    }
}