package com.endlesspassion.sigai.domain.store.service;

import com.endlesspassion.sigai.domain.store.dto.request.StoreRevenueReq;
import com.endlesspassion.sigai.domain.store.dto.respose.StoreRevenueRes;
import com.endlesspassion.sigai.domain.store.entity.Store;
import com.endlesspassion.sigai.domain.store.entity.StoreRevenue;
import com.endlesspassion.sigai.domain.store.repository.StoreRepository;
import com.endlesspassion.sigai.domain.store.repository.StoreRevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class StoreRevenueService {

    private final StoreRevenueRepository storeRevenueRepository;
    private final StoreRepository storeRepository;

    /**
     * 특정 가게의 모든 월별 매출 데이터 조회
     */
    public StoreRevenueRes getAllByStoreId(Long storeId) {
        // 가게 존재 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + storeId));

        // 월별 매출 데이터 조회 (최신순 정렬)
        List<StoreRevenue> storeRevenues = storeRevenueRepository.findByStoreOrderByYearDescMonthDesc(store);

        if (storeRevenues.isEmpty()) {
            throw new IllegalArgumentException("해당 가게의 매출 데이터가 없습니다. ID: " + storeId);
        }

        return StoreRevenueRes.from(storeRevenues);
    }

    /**
     * 월별 매출 데이터 저장
     */
    @Transactional
    public void create(StoreRevenueReq req) {
        // 가게 존재 확인
        Store store = storeRepository.findById(req.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다. ID: " + req.getStoreId()));

        // 고객 비중 검증
        req.validateCustomerRatios();

        // StoreRevenue 엔티티 생성 및 저장
        storeRevenueRepository.save(req.to(store));
    }
}
