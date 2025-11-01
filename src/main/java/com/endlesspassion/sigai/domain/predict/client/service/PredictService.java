package com.endlesspassion.sigai.domain.predict.client.service;

import com.endlesspassion.sigai.domain.predict.ai.AiPredictService;
import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictReq;
import com.endlesspassion.sigai.domain.predict.ai.dto.AiPredictRes;
import com.endlesspassion.sigai.domain.predict.client.dto.ChangedClose;
import com.endlesspassion.sigai.domain.predict.client.dto.ChangedRevenue;
import com.endlesspassion.sigai.domain.predict.client.dto.PredictReq;
import com.endlesspassion.sigai.domain.predict.client.dto.PredictRes;
import com.endlesspassion.sigai.domain.publicdata.document.PublicProfitData;
import com.endlesspassion.sigai.domain.publicdata.document.PublicStoreData;
import com.endlesspassion.sigai.domain.publicdata.service.PublicDataService;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import com.endlesspassion.sigai.domain.store.repository.StoreRevenueRepository;
// [수정] import 경로를 global.utils -> domain.predict.client.service로 변경
import com.endlesspassion.sigai.domain.predict.client.service.QuarterUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PredictService {

    private final AiPredictService aiPredictService;
    private final PublicDataService publicDataService;
    private final StoreRepository storeRepository;
    private final StoreRevenueRepository storeRevenueRepository;
    private final QuarterUtil quarterUtil; // (주입)

    /**
     * 폐업 예측 메인 서비스 (Facade Method)
     *
     * @param req (storeId, quarter, monthsOfOperation)
     * @return PredictRes (예측 결과 + 분기 대비 변화 DTO)
     */
    public PredictRes predict(PredictReq req) {

        String currentQuarter = req.getQuarter();
        // [수정] QuarterUtil import가 정상화되어 'getPreviousQuarter'가 인식됩니다.
        String previousQuarter = quarterUtil.getPreviousQuarter(currentQuarter); // 예: "20241" -> "20234"

        // 1. 예측할 가게 정보와 (사장님이 입력한) 최근 매출 정보 가져오기
        log.info("Step 1/5: 가게 정보 조회 (Store ID: {})", req.getStoreId());
        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다: " + req.getStoreId()));

        // [수정] Repository에 'findFirst...' 메소드를 추가할 것이므로 이 코드는 유효합니다.
        StoreRevenue storeRevenue = storeRevenueRepository.findFirstByStoreOrderByYearDescMonthDesc(store)
                .orElseThrow(() -> new EntityNotFoundException("매출 정보를 찾을 수 없습니다: "));

        String trdarCd = store.getServiceArea().getCode();
        String svcIndutyCd = store.getServiceIndustry().getCode();

        // 2. [매출] 관련 공공데이터 조회
        log.info("Step 2/5: 공공데이터 [매출] 분석 (상권: {}, 업종: {}, 분기: {} vs {})", trdarCd, svcIndutyCd, currentQuarter, previousQuarter);
        PublicProfitData currentProfit = publicDataService.getProfitData(currentQuarter, trdarCd, svcIndutyCd);
        PublicProfitData previousProfit = publicDataService.getProfitData(previousQuarter, trdarCd, svcIndutyCd);

        // 3. [폐업] 관련 공공데이터 조회
        log.info("Step 3/5: 공공데이터 [폐업] 분석 (상권: {}, 업종: {}, 분기: {} vs {})", trdarCd, svcIndutyCd, currentQuarter, previousQuarter);
        PublicStoreData currentStore = publicDataService.findStoreData(currentQuarter, trdarCd, svcIndutyCd);
        PublicStoreData previousStore = publicDataService.findStoreData(previousQuarter, trdarCd, svcIndutyCd);

        // 4. AI 요청용 데이터 추출 (헬퍼 함수 사용)
        Double[] aiRevenueMetrics = getAiRevenueMetrics(currentProfit);
        Double aiClosureMetric = getAiClosureMetric(currentStore);

        // 5. AI 서버에 폐업률 예측 요청
        log.info("Step 4/5: AI 서버에 예측 요청");
        AiPredictReq aiPredictReq = AiPredictReq.to(
                req, store, storeRevenue,
                aiRevenueMetrics[0], // [0] = 매출 순위
                aiRevenueMetrics[1], // [1] = 매출 비율
                aiRevenueMetrics[2], // [2] = 매출 건수 비율
                aiClosureMetric,
                quarterUtil
        );
        AiPredictRes aiPredictRes = aiPredictService.predict(aiPredictReq);

        // 6. 프론트엔드 응답용 "전분기 대비" DTO 생성 (헬퍼 함수 사용)
        ChangedRevenue changedRevenue = getChangedRevenue(currentProfit, previousProfit);
        ChangedClose changedClose = getChangedClose(currentStore, previousStore);

        // 7. 최종 응답 조립 (AI 결과 + 전분기 대비 변화)
        log.info("Step 5/5: 최종 응답 조립");
        return PredictRes.of(
                aiPredictRes,
                changedRevenue,
                changedClose
        );
    }

    // --- AI 요청용 데이터 추출 헬퍼 ---

    private Double[] getAiRevenueMetrics(PublicProfitData profitData) {
        if (profitData == null) {
            log.warn("현재 분기 매출 공공데이터가 없어 기본값으로 대체합니다.");
            return new Double[]{80.0, 0.1, 0.1};
        }

        // (TODO) AI 매출 지표 계산 로직 구현 필요
        log.warn("AI 매출 지표(순위, 비율) 계산 로직이 누락되었습니다. 임시 기본값(80.0, 0.1, 0.1)을 사용합니다.");
        Double rank = 80.0;
        Double ratio = 0.1;
        Double countRatio = 0.1;

        return new Double[]{rank, ratio, countRatio};
    }

    private Double getAiClosureMetric(PublicStoreData storeData) {
        // [수정] getClsbizStorCo -> getClsbizCo
        if (storeData == null || storeData.getStorCo() == null || storeData.getClsbizStorCo() == null || storeData.getStorCo() == 0) {
            log.warn("현재 분기 점포 공공데이터가 없거나 분모가 0이므로 기본값(0.5)으로 대체합니다.");
            return 0.5;
        }
        // [수정] getClsbizStorCo -> getClsbizCo
        return storeData.getClsbizStorCo().doubleValue() / storeData.getStorCo().doubleValue();
    }

    // --- 프론트 응답용 "전분기 대비" DTO 생성 헬퍼 ---

    private ChangedRevenue getChangedRevenue(PublicProfitData current, PublicProfitData previous) {
        if (current == null || previous == null) {
            return ChangedRevenue.empty();
        }

        Integer countChange = null;
        if (current.getThsmonSelngCo() != null && previous.getThsmonSelngCo() != null) {
            countChange = (int) (current.getThsmonSelngCo() - previous.getThsmonSelngCo());
        }

        Integer rankChange = null;
        Float percentileChange = null;

        return ChangedRevenue.of(rankChange, percentileChange, countChange);
    }

    private ChangedClose getChangedClose(PublicStoreData current, PublicStoreData previous) {
        // [수정] getClsbizStorCo -> getClsbizCo (필드명 통일)
        if (current == null || previous == null || current.getStorCo() == null || current.getClsbizStorCo() == null ||
                previous.getStorCo() == null || previous.getClsbizStorCo() == null ||
                current.getStorCo() == 0 || previous.getStorCo() == 0) {
            return ChangedClose.empty();
        }

        // [수정] getClsbizStorCo -> getClsbizCo
        Float currentRate = (float) (current.getClsbizStorCo().doubleValue() / current.getStorCo().doubleValue() * 100.0);
        // [수정] getClsbizStorCo -> getClsbizCo
        Float previousRate = (float) (previous.getClsbizStorCo().doubleValue() / previous.getStorCo().doubleValue() * 100.0);
        Integer rateChange = (int) (currentRate - previousRate);

        return ChangedClose.of(currentRate, rateChange);
    }
}